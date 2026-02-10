package backend.knowhow.global.security.jwt;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.global.security.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtInterceptor implements ChannelInterceptor {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) return message;

        // STOMP CONNECT 프레임이면 인증 시도
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("[WS][CONNECT] nativeHeaders={}", accessor.toNativeHeaderMap());

            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null) {
                authHeader = accessor.getFirstNativeHeader("authorization");
            }

            // 2) authHeader 존재 여부 + Bearer 여부만 확인
            log.info("[WS][CONNECT] hasAuthHeader={}, startsWithBearer={}",
                    authHeader != null,
                    authHeader != null && authHeader.startsWith("Bearer ")
            );


            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("[WS][CONNECT] Missing/invalid Authorization header");
                throw new IllegalArgumentException("Missing Authorization header");
            }

            String token = authHeader.substring(7);
            // 3) validate 단계 진입 확인
            Long memberId;
            try {
                memberId = jwtUtil.validateAndExtractMemberId(token);
                log.info("[WS][CONNECT] token validated, memberId={}", memberId);
            } catch (Exception e) {
                log.warn("[WS][CONNECT] token validation failed: {}", e.getClass().getSimpleName());
                throw e;
            }

            // 4) DB 조회 결과 확인
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> {
                        log.warn("[WS][CONNECT] member not found. memberId={}", memberId);
                        return new IllegalArgumentException("Member not found");
                    });

            // 5) setUser 적용 확인
            MemberPrincipal principal = new MemberPrincipal(member);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    principal, null, Collections.emptyList()
            );
            accessor.setUser(auth);
            log.info("[WS][CONNECT] setUser done. principalType={}", principal.getClass().getSimpleName());


            return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
        }

        return message;
    }
}