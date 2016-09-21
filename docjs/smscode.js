/**
 * @api {post} /smsCode 发送
 * @apiVersion 1.0.0
 * @apiName PostSmsCode
 * @apiGroup SmsCode
 *
 * @apiParam {String} phone     要发送短信验证码的手机号
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: "1234567",      // smsid，可用于查询短信发送状态
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "1004",
 *      data: "",             
 *      msg:  "手机号码无效"
 *    }
 */

/**
 * @api {post} /smsCode/verification 验证
 * @apiVersion 1.0.0
 * @apiName PostSmscodeVerification
 * @apiGroup SmsCode
 *
 * @apiParam {String} phone     要验证短信验证码的手机号
 * @apiParam {String} smsCode   待验证的验证码
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: "",             
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "3000",
 *      data: "",
 *      msg:  "验证码错误或者已被验证"
 *    }
 */

/**
 * @api {get} /smsCode/state?smsid=XXXX 查询状态
 * @apiVersion 1.0.0
 * @apiName GetSmsCodeState
 * @apiGroup SmsCode
 *
 * @apiParam {String} smsid 成功发送短信验证码后收到的id
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {
 *        sendState: 'SUCCESS',     // 发送状态，SENDING-发送中，FAIL-发送失败，SUCCESS-发送成功
 *        verifyState: true         // 验证状态，true-已验证 false-未验证
 *      },
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "3000",
 *      data: "",
 *      msg:  "验证码错误或者已被验证"
 *    }
 */