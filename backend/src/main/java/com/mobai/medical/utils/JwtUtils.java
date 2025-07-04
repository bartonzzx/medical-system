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
    public static final String APP_SECRET = "ukc8BDbRigUDaY6pZFfWus2jZWLPH0"; // 密钥，可自定义

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
      System.out.println("这里是JwtUtils.getjwtToken，正在生成Token......");
      System.out.println("生成Token为： "+JwtToken);
      return JwtToken;
    }

    // 判断Token是否有效的方法：两个方法，但是参数不一样
    public static boolean checkToken(String jwtToken) {
      System.out.println("这里是JwtUtils.checkToken，正在检验Token......");
      if (!StringUtils.hasText(jwtToken)) return false;
      try {
        Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken); // 解析token
      } catch (Exception e) {
        // e.printStackTrace();
        return false; // 如果解析失败，返回false
      }
      System.out.println("检验成功！");
      return true; // 如果解析成功，返回true
    }

    public static boolean checkToken(HttpServletRequest request) {
      System.out.println("这里是JwtUtils.checkToken，正在检验Token......");
      try {
        String jwtToken = request.getHeader("token");
        if (!StringUtils.hasText(jwtToken)) return false;
        Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken); // 解析token
      } catch (Exception e) {
        // e.printStackTrace();
        return false; // 如果解析失败，返回false
      }
      System.out.println("检验成功！");
      return true; // 如果解析成功，返回true
    }

    // 获取Claims的方法
    public static Claims getClaims(String jwtToken) {
      System.out.println("这里是JwtUtils.checkToken，正在获取claims......");
      if (!StringUtils.hasText(jwtToken)) return null; // 如果Token为空，返回null
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
      Claims claims = claimsJws.getBody();
      System.out.println("获取claims成功！");
      return claims;
    }

    // 根据Token获取用户ID的方法
    public static String getMemberIdByJwtToken(HttpServletRequest request) {
      System.out.println("这里是JwtUtils.getMemberIdByJwtToken，正在根据Token解析用户id......");
      String jwtToken = request.getHeader("token");
      if (!StringUtils.hasLength(jwtToken)) return null;
      Jws<Claims> claimsJws = Jwts.parser().setSigningKey(APP_SECRET).parseClaimsJws(jwtToken);
      Claims claims = claimsJws.getBody();
      System.out.println("获取id成功，id为："+claims.get("id"));
      return claims.get("id").toString();
    }
  }