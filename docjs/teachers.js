/**
 * @api {get} /teachers 获取所有
 * @apiVersion 1.0.0
 * @apiName GetTeacher
 * @apiGroup Teacher
 * @apiDescription 仅供调试。
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],         // 所有的教师
 *      msg:  "操作成功"
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
 * @api {put} /teachers/:id 修改
 * @apiVersion 1.0.0
 * @apiName PutTeacher
 * @apiGroup Teacher
 * @apiDescription 根据指定字段名和值修改指定id的教师，禁止修改id和手机号
 * 
 * @apiSuccess {String}  code  200表示成功。
 * @apiSuccess {Object}  data  修改后的教师信息
 * 
 * @apiParam {String} name      姓名（可选）
 * @apiParam {String} school    学校（可选）
 * @apiParam {String} academy   学院（可选）
 * @apiParam {String} avatar    头像url（可选）
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},         // 修改后的教师信息
 *      msg:  "操作成功"
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
 * @api {post} /teachers/search 查询
 * @apiVersion 1.0.0
 * @apiName PostTeacherSearch
 * @apiGroup Teacher
 * @apiDescription 根据指定字段名和值查询教师。（必须要有查询条件）
 * 
 * @apiParam {String} _id       教师id（可选）
 * @apiParam {String} phone     手机号码（可选） 
 * @apiParam {String} name      姓名（可选）
 * @apiParam {String} school    学校（可选）
 * @apiParam {String} academy   学院（可选）
 * @apiParam {String} avatar    头像url（可选）
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],         // 查询到的所有教师
 *      msg:  "操作成功"
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