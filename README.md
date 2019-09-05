# Spring Security Examples

## security-thymelaf
Basic Form Login Page with Remember Me

---

## security-rest1
Basic Rest Api

---

## security-rest2
Basic Rest Api with multiple configurations

---

## security-rest3
Basic Rest Api with multiple configurations and JWT

#### Login
```
GET /login
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkb21haW4uY29tIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6IlNhbmRlciIsImV4cCI6MTU2NzY0MjQ3Miwicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdfQ.kVm0q2TlK-7IZyAyHhT2J93yGslBKIARWB7AdOYhCGWexmzNVkgAN2vJ5uwaRV7rBXHdvzLYKstwzaaoJV9O0g

{
    "username": "Sander",
    "status": "ACTIVE",
    "roles": [
        "ROLE_USER",
        "ROLE_ADMIN"
    ],
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkb21haW4uY29tIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6IlNhbmRlciIsImV4cCI6MTU2NzY0MjQ3Miwicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdfQ.kVm0q2TlK-7IZyAyHhT2J93yGslBKIARWB7AdOYhCGWexmzNVkgAN2vJ5uwaRV7rBXHdvzLYKstwzaaoJV9O0g",
    "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkb21haW4uY29tIiwianRpIjoiNDU2M2E1NjgtZTA4YS00ZjJkLWE1YzYtNWJhM2VhMmNjYjk3IiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6IlNhbmRlciIsImV4cCI6MTU2NzcyODgxMiwicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdfQ.3TKDq-FIqdsRaPFLYiPClT796ZSn1on5YmWSfWj9qJP5DtsUGpZL_bZz69yepGKVsgkzHgO4asio51H-u2REjw"
}
```

#### Refresh Token
```
GET /refresh-token
{
    "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkb21haW4uY29tIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6IlNhbmRlciIsImV4cCI6MTU2NzY0MjI4Miwicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdfQ.5OMnTinxlMo_6rjabmsrxvzPZsIQXeFzJ0lV0osA_Dlo8NR8-tyYAxWza-vR0OLKkkSO2BekorFdhZvFN2OD0Q",
    "refreshToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJkb21haW4uY29tIiwianRpIjoiOTJiNmJmMzMtMjAwOC00ZGZmLTg2N2EtZGVkYzYxYjFmZTgyIiwiYXVkIjoic2VjdXJlLWFwcCIsInN1YiI6IlNhbmRlciIsImV4cCI6MTU2NzcyODYyMiwicm9sZXMiOlsiUk9MRV9VU0VSIiwiUk9MRV9BRE1JTiJdfQ.aBIbcEhrVgNaQ3zXf4ScKAYN07qn6WIcpJp4MRe5ESyYC7zJ3vP9kMRugt1EMA2h4mev6Hz98uowKLWVhNQ6hg"
}
```

#### Other endpoints for security testing
```
GET /admin/test1
GET /users/test1
POST /users/test7
```

---
