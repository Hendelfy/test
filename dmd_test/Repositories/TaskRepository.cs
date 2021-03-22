using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using dmd_test.Models;
using dmd_test.ViewModels;
using Microsoft.EntityFrameworkCore;
using TaskStatus = dmd_test.Models.TaskStatus;

namespace dmd_test.Repositories
{
    public class TaskRepository : ITaskRepository
    {
        private readonly TaskDbSet _context;

        public TaskRepository(TaskDbSet context)
        {
            _context = context;
        }

        public async Task<IEnumerable<TaskModel>> GetAll()
        {
            return (await _context.Tasks
                    .Include(t => t.SubTasks)
                    .ToListAsync())
                .Where(t => t.ParentTaskId == null);
        }

        public async Task<TaskModel> GetById(Guid id)
        {
            return await _context.Tasks
                .Include(t => t.SubTasks)
                .FirstOrDefaultAsync(t => t.Id == id);
        }

        public async Task<int> Update(TaskModel task)
        {
            _context.Tasks.Update(task);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> Delete(TaskModel task)
        {
            _context.Tasks.Remove(task);
            return await _context.SaveChangesAsync();
        }

        public async Task<int> Create(TaskModel task)
        {
            _context.Tasks.Add(task);
            return await _context.SaveChangesAsync();
        }

        public async Task<bool> Exists(Guid id)
        {
            return await _context.Tasks.AnyAsync(t => t.Id == id);
        }
    }
}