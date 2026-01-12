package backend.knowhow.global.security.jwt;

import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import backend.knowhow.global.security.MemberPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authHeader.substring(7);
            Long memberId = jwtUtil.validateAndExtractMemberId(token);
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
            SimpleGrantedAuthority authority =
                    new SimpleGrantedAuthority("ROLE_" + member.getRole().name());

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            new MemberPrincipal(member),
                            null,
                            List.of(authority)
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
        catch (BaseException e) {
            setErrorResponse(response, e.getErrorType());
        }
        catch (Exception e) {
            setErrorResponse(response, ErrorType.INTERNAL_SERVER_ERROR);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ErrorType errorType) throws IOException {

        response.setStatus(errorType.getHttpStatus().value());
        response.setContentType("application/json; charset=UTF-8");

        String json = String.format(
                "{ \"status\": %d, \"code\": \"%s\", \"message\": \"%s\", \"data\": null }",
                errorType.getHttpStatus().value(),
                errorType.getCode(),
                errorType.getMessage()
        );

        response.getWriter().write(json);
    }
}
