package backend.knowhow.domain.auth.dto.response;

public record GoogleUserInfo (
    String sub,
    String nickname
)
{}
