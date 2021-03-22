using System.Linq;
using dmd_test.Models;

namespace dmd_test.Helpers
{
    public static class Helpers
    {
        public static bool InProgressToCompleted(TaskModel task)
        {
            if (task.Status != TaskStatus.InProgress)
                return false;

            if (task.SubTasks != null && task.SubTasks.Any())
            {
                foreach (var subTask in task.SubTasks)
                {
                    if (!InProgressToCompleted(subTask))
                    {
                        return false;
                    }
                }
            }

            task.Status = TaskStatus.Completed;
            return true;
        }
    }
}