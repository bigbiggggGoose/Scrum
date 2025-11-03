/**
 * 项目迭代表 api 封装
 *
 * @Author:    cmz
 * @Date:      2025-10-11 22:41:18
 * @Copyright  1
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const scrumSprintsApi = {

  /**
   * 分页查询  @author  cmz
   */
  queryPage : (param) => {
    return postRequest('/scrumSprints/queryPage', param);
  },

  /**
   * 增加  @author  cmz
   */
  add: (param) => {
      return postRequest('/scrumSprints/add', param);
  },

  /**
   * 修改  @author  cmz
   */
  update: (param) => {
      return postRequest('/scrumSprints/update', param);
  },


  /**
   * 删除  @author  cmz
   */
  delete: (id) => {
      return getRequest(`/scrumSprints/delete/${id}`);
  },

  /**
   * 批量删除  @author  cmz
   */
  batchDelete: (idList) => {
      return postRequest('/scrumSprints/batchDelete', idList);
  },

};
