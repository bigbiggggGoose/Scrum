/**
 * Sprint待办事项表 api 封装
 *
 * @Author:    system
 * @Date:      2025-01-01 00:00:00
 * @Copyright  system
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const scrumSprintBacklogApi = {

  /**
   * 分页查询  @author  system
   */
  queryPage : (param) => {
    return postRequest('/scrumSprintBacklog/queryPage', param);
  },

  /**
   * 增加  @author  system
   */
  add: (param) => {
      return postRequest('/scrumSprintBacklog/add', param);
  },

  /**
   * 修改  @author  system
   */
  update: (param) => {
      return postRequest('/scrumSprintBacklog/update', param);
  },

  /**
   * 删除  @author  system
   */
  delete: (id) => {
      return getRequest(`/scrumSprintBacklog/delete/${id}`);
  },

  /**
   * 根据Sprint ID查询待办事项  @author  system
   */
  queryBySprintId: (sprintId) => {
      return getRequest(`/scrumSprintBacklog/queryBySprintId/${sprintId}`);
  },

};