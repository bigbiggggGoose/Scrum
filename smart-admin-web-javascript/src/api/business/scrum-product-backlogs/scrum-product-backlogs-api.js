/**
 * 产品待办事项表 api 封装
 *
 * @Author:    cmz
 * @Date:      2025-10-11 22:40:46
 * @Copyright  1
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const scrumProductBacklogsApi = {

  /**
   * 分页查询  @author  cmz
   */
  queryPage : (param) => {
    return postRequest('/scrumProductBacklogs/queryPage', param);
  },

  /**
   * 增加  @author  cmz
   */
  add: (param) => {
      return postRequest('/scrumProductBacklogs/add', param);
  },

  /**
   * 修改  @author  cmz
   */
  update: (param) => {
      return postRequest('/scrumProductBacklogs/update', param);
  },


  /**
   * 删除  @author  cmz
   */
  delete: (id) => {
      return getRequest(`/scrumProductBacklogs/delete/${id}`);
  },

  /**
   * 批量删除  @author  cmz
   */
  batchDelete: (idList) => {
      return postRequest('/scrumProductBacklogs/batchDelete', idList);
  },

};
