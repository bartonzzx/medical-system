  package com.mobai.medical.utils;

  import io.jsonwebtoken.Claims;
  import io.jsonwebtoken.Jws;
  import io.jsonwebtoken.Jwts;
  import io.jsonwebtoken.SignatureAlgorithm;
  import org.springframework.util.StringUtils;

  import javax.servlet.http.HttpServletRequest;
  import java.util.Date;

  public class JwtUtils {

    // 生成Token的方法
    public static final long EXPIRE_TIME = 1000 * 60 * 60 * 6; // 过期时间：6小时
    public static final String APP_SECRET = "mobaisilent"; // 密钥

    public static String getJwtToken(Long id, String uname, String role) {
      String JwtToken = Jwts.builder()
              .setHeaderParam("typ", "JWT") // jwt头部信息
              .setHeaderParam("alg", "HS256")
              .setSubject("medical-user") // 分类（自定义）
              .setIssuedAt(new Date())
              .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
              .claim("id", id) // token载体（用户名和用户id
              .claim("uname", uname)
              .claim("role", role)
              .signWith(SignatureAlgorithm.HS256, APP_SECRET)
              .compact(); // 生成JWT字符串
      return JwtToken;
    }

    // 判断Token是否有效的方法：两个方法，但是参数不一样
    public static boolean checToken(String jwtToken) {
      if (!StringUtils.hasText(jwtToken)) return false;
      try {
        Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken); // 解析token
      } catch (Exception e) {
        // e.printStackTrace();
        return false; // 如果解析失败，返回false
      }
      return true; // 如果解析成功，返回true
    }

    public static boolean checkToken(HttpServletRequest request) {
      try {
        String jwtToken = request.getHeader("token");
        if (!StringUtils.hasText(jwtToken)) return false;
        Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken); // 解析token
      } catch (Exception e) {
        // e.printStackTrace();
        return false; // 如果解析失败，返回false
      }
      return true; // 如果解析成功，返回true
    }

    // 获取Claims的方法
    public static Claims getClaims(String jwtToken) {
      if (!StringUtils.hasText(jwtToken)) return null; // 如果Token为空，返回null
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
      Claims claims = claimsJws.getBody();
      return claims;
    }

    // 根据Token获取用户ID的方法
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
      String jwtToken = request.getHeader("token");
      if (!StringUtils.hasLength(jwtToken)) return "";
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
      Claims claims = claimsJws.getBody();
      return claims.get("id").toString();
    }
  }