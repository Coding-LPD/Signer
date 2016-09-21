/**
 * @api {get} /courses 获取所有
 * @apiVersion 1.0.0
 * @apiName GetCourse
 * @apiGroup Course
 * @apiDescription 仅供调试。
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],           // 所有课程
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],           // 空数组
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /courses 创建
 * @apiVersion 1.0.0
 * @apiName PostCourse
 * @apiGroup Course
 * 
 * @apiParam {String} name          课程名称
 * @apiParam {String} startTime     开始时间（格式：2016-06-11）
 * @apiParam {String} endTime       结束时间（格式：2016-06-11）
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},           // 创建后的课程信息
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},           // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {delete} /courses/:id 删除
 * @apiVersion 1.0.0
 * @apiName DeleteCourse
 * @apiGroup Course
 * 
 * @apiSuccessExample 成功
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: {},         // 删除的课程的所有信息
 *      msg:  "操作成功"
 *    }
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: {},           // 空对象
 *      msg:  "错误信息"
 *    }
 */

/**
 * @api {post} /courses/search 查询
 * @apiVersion 1.0.0
 * @apiName PostCourseSearch
 * @apiGroup Course
 * @apiDescription 根据指定字段名和值查询课程。（必须要有查询条件）
 * 
 * @apiParam {String} _id             课程id（可选）
 * @apiParam {String} name            课程名称（可选）
 * @apiParam {String} teacherId       所属教师id（可选）
 * @apiParam {String} location        上课地点（可选）（excel导入）
 * @apiParam {String} academy         学院（可选）（excel导入）
 * @apiParam {String} time            上课时间（可选）（excel导入）
 * @apiParam {String} startTime       课程起始时间（创建时指定）
 * @apiParam {String} endTime         课程结束时间（创建时指定）
 * @apiParam {Number} studentCount    学生数量
 * @apiParam {Number} signCount       签到次数
 * @apiParam {Number} state           课程状态（0：未开始，1：进行中，2：已结束）
 * 
 * @apiSuccessExample 成功：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "200",
 *      data: [],           // 查询到的所有课程
 *      msg:  "操作成功"
 *    } 
 * 
 * @apiErrorExample 失败：
 *    HTTP/1.1 200 OK
 *    {
 *      code: "XXXX",
 *      data: [],           // 空数组
 *      msg:  "错误信息"
 *    }
 */