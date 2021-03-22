using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using dmd_test.Models;

namespace dmd_test.Repositories
{
    public interface ITaskRepository
    {
        Task<IEnumerable<TaskModel>> GetAll();
        Task<TaskModel> GetById(Guid updateDto);
        Task<int> Update(TaskModel task);
        Task<int> Delete(TaskModel task);
        Task<int> Create(TaskModel task);
        Task<bool> Exists(Guid id);
    }
}