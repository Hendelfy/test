using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using dmd_test.Models;

namespace dmd_test.ViewModels
{
    public class TaskAddDTO
    {
        [Required]
        public string Name { get; set; }
        [Required]
        public string Desc { get; set; }
        [Required]
        public string Performers { get; set; }
        [Required]
        public DateTime? StartTime { get; set; }
        [Required]
        public int? PlannedDifficulty { get; set; }
        public DateTime? FinishTime { get; set; }
        public Guid? ParentTaskId { get; set; }

        public TaskModel ToTaskModel()
        {
            return new TaskModel
            {
                Name = Name,
                Desc = Desc,
                Performers = Performers,
                StartTime = StartTime!.Value,
                PlannedDifficulty = PlannedDifficulty!.Value,
                FinishTime = FinishTime,
                ParentTaskId = ParentTaskId
            };
        }
    }
}