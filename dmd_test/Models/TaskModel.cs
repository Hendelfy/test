using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Text.Json.Serialization;
using Microsoft.EntityFrameworkCore;

namespace dmd_test.Models
{
    public enum TaskStatus
    {
        Assigned, InProgress, Paused, Completed
    }
    public class TaskModel
    {
        [Key]
        [DatabaseGenerated(DatabaseGeneratedOption.Identity)]
        public Guid Id { get; set; }
        [Required]
        public string Name { get; set; }
        [Required]
        public string Desc { get; set; }
        [Required]
        public string Performers { get; set; }
        [Required]
        public DateTime StartTime { get; set; }
        public TaskStatus Status { get; set; }
        [Required]
        public int PlannedDifficulty { get; set; }
        public DateTime? ExecutionTime { get; set; }
        public DateTime? FinishTime { get; set; }
        public IEnumerable<TaskModel> SubTasks { get; set; }
        public Guid? ParentTaskId { get; set; }
        [JsonIgnore]
        public TaskModel ParentTask { get; set; }
        
    }
}   