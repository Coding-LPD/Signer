/**
 * @api {post} /users 注册
 * @apiVersion 1.0.0
 * @apiName PostUser
 * @apiGroup User
 * @apiDescription 注册成功，返回的响应头部的cookie中包含有后续请求所需要的access_token。
 *
 * @apiParam {String} phone     用户手机号码
 * @apiParam {String} password  密码（必须用服务器提供的公钥进行加密）
 * @apiParam {String} role      角色（“0”表示学生，“1”表示教师）
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: { user: {}, person: {} },         // user注册成功的用户的信息,person为注册成功的教师或学生信息
 *      msg:  "请求成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},         // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /users/login 密码登录
 * @apiVersion 1.0.0
 * @apiName PostUserLogin
 * @apiGroup User
 * @apiDescription 登录成功，返回的响应头部的cookie中包含有后续请求所需要的access_token。
 * 
 * @apiParam {String} phone     用户手机号码
 * @apiParam {String} password  密码（必须用服务器提供的公钥进行加密）
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: { user: {}, person: {} }          // user登录成功的用户的信息，person为登录成功的教师或学生信息
 *      msg:  "请求成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},         // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /users/loginWithSmsCode 验证码登录
 * @apiVersion 1.0.0
 * @apiName PostUserLoginWithSmsCode
 * @apiGroup User
 * @apiDescription 登录成功，返回的响应头部的cookie中包含有后续请求所需要的access_token。
 * 
 * @apiParam {String} phone     用户手机号码
 * @apiParam {String} smsCode   验证码
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: { user: {}, person: {} }          // user登录成功的用户的信息，person为登录成功的教师或学生信息
 *      msg:  "请求成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},         // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /users/search 查询
 * @apiVersion 1.0.0
 * @apiName PostUserSearch
 * @apiGroup User
 * @apiDescription 根据指定字段名和值查询用户，返回的用户对象不包含密码字段。
 * 
 * @apiParam {String} _id       用户id（可选）
 * @apiParam {String} phone     用户手机号码（可选）
 * @apiParam {String} role      用户角色（可选）
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],         // 查询到的所有用户
 *      msg:  "请求成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],         // 空数组
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {put} /users/:id 修改密码
 * @apiVersion 1.0.0
 * @apiName PutUser
 * @apiGroup User
 * @apiDescription 修改指定id用户的密码。
 * 
 * @apiParam {String} password  密码（必须用服务器提供的公钥进行加密）
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},         // 修改密码的用户信息，不包含密码
 *      msg:  "请求成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},         
 *      msg:  "错误信息"
 *    }
 */