/**
 * Sprint与产品待办中间表 api 封装
 *
 * @Author:    cmz
 * @Date:      2025-10-11 22:41:46
 * @Copyright  1
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const scrumProductSprintBacklogApi = {

  /**
   * 分页查询  @author  cmz
   */
  queryPage : (param) => {
    return postRequest('/scrumProductSprintBacklog/queryPage', param);
  },

  /**
   * 增加  @author  cmz
   */
  add: (param) => {
      return postRequest('/scrumProductSprintBacklog/add', param);
  },

  /**
   * 修改  @author  cmz
   */
  update: (param) => {
      return postRequest('/scrumProductSprintBacklog/update', param);
  },


  /**
   * 删除  @author  cmz
   */
  delete: (id) => {
      return getRequest(`/scrumProductSprintBacklog/delete/${id}`);
  },

  /**
   * 批量删除  @author  cmz
   */
  batchDelete: (idList) => {
      return postRequest('/scrumProductSprintBacklog/batchDelete', idList);
  },

};
