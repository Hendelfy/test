using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using dmd_test.Models;

namespace dmd_test.ViewModels
{
    public class TaskUpdateDTO
    {
        [Required] public Guid? Id { get; set; }
        [Required] public string Name { get; set; }
        [Required] public string Desc { get; set; }
        [Required] public string Performers { get; set; }
        [Required] public DateTime? StartTime { get; set; }
        [Required]
        [Range(0,3)]
        public TaskStatus? Status { get; set; }
        [Required] public int? PlannedDifficulty { get; set; }
        public DateTime? FinishTime { get; set; }


        public void CopyPropsTo(TaskModel task)
        {
            task.Id = Id!.Value;
            task.Name = Name;
            task.Desc = Desc;
            task.Performers = Performers;
            task.StartTime = StartTime!.Value;
            task.Status = Status!.Value;
            task.PlannedDifficulty = PlannedDifficulty!.Value;
            if (FinishTime != null)
                task.FinishTime = FinishTime;
        }
    }
}