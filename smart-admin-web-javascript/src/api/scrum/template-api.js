/**
 * 站内信/邮件模板表 api 封装
 *
 * @Author:    thr
 * @Date:      2025-10-12 14:33:45
 * @Copyright  wu
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const templateApi = {

  /**
   * 分页查询  @author  thr
   */
  queryPage : (param) => {
    return postRequest('/template/queryPage', param);
  },

  /**
   * 增加  @author  thr
   */
  add: (param) => {
      return postRequest('/template/add', param);
  },

  /**
   * 修改  @author  thr
   */
  update: (param) => {
      return postRequest('/template/update', param);
  },


  /**
   * 删除  @author  thr
   */
  delete: (id) => {
      return getRequest(`/template/delete/${id}`);
  },

  /**
   * 批量删除  @author  thr
   */
  batchDelete: (idList) => {
      return postRequest('/template/batchDelete', idList);
  },

};
