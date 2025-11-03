/**
 * 系统动态表（全局操作记录） api 封装
 *
 * @Author:    a
 * @Date:      2025-09-30 10:32:40
 * @Copyright  a
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const sysActivitiesApi = {

  /**
   * 分页查询  @author  a
   */
  queryPage : (param) => {
    return postRequest('/sysActivities/queryPage', param);
  },

  /**
   * 增加  @author  a
   */
  add: (param) => {
      return postRequest('/sysActivities/add', param);
  },

  /**
   * 修改  @author  a
   */
  update: (param) => {
      return postRequest('/sysActivities/update', param);
  },


  /**
   * 删除  @author  a
   */
  delete: (id) => {
      return getRequest(`/sysActivities/delete/${id}`);
  },

  /**
   * 批量删除  @author  a
   */
  batchDelete: (idList) => {
      return postRequest('/sysActivities/batchDelete', idList);
  },

};
