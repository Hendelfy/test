using Microsoft.EntityFrameworkCore;

namespace dmd_test.Models
{
    public class TaskDbSet : DbContext
    {
        public TaskDbSet(DbContextOptions<TaskDbSet> options) : base(options)
        {
            
        }

        public DbSet<TaskModel> Tasks { get; set; }
    }
}