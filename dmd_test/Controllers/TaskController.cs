using System;
using System.Threading.Tasks;
using dmd_test.Repositories;
using dmd_test.ViewModels;
using Microsoft.AspNetCore.Mvc;
using TaskStatus = dmd_test.Models.TaskStatus;

namespace dmd_test.Controllers
{
    [ApiController]
    [Route("api/[controller]")]
    public class TaskController : ControllerBase
    {
        private readonly ITaskRepository _repository;

        public TaskController(ITaskRepository repository)
        {
            _repository = repository;
        }

        [HttpGet]
        public async Task<IActionResult> GetAll()
        {
            var list = await _repository.GetAll();
            return Ok(list);
        }

        [HttpGet("{id}")]
        public async Task<IActionResult> GetById(Guid id)
        {
            var task = await _repository.GetById(id);
            if (task == null) return NotFound();
            return Ok(task);
        }

        [HttpPost]
        public async Task<IActionResult> Add(TaskAddDTO taskAddDto)
        {
            var task = taskAddDto.ToTaskModel();
            task.Id = new Guid();
            await _repository.Create(task);
            return Created(task.Id.ToString(), task);
        }

        [HttpPut]
        public async Task<IActionResult> Update(TaskUpdateDTO updateDto)
        {
            var task = await _repository.GetById(updateDto.Id!.Value);
            if (task == null) return NotFound();

            if (task.Status != updateDto.Status)
            {
                if (updateDto.Status == TaskStatus.Completed && !Helpers.Helpers.InProgressToCompleted(task))
                {
                    return BadRequest(new {message = "All tasks should be in progress"});
                }

                if (updateDto.Status == TaskStatus.Paused && task.Status != TaskStatus.InProgress)
                {
                    return BadRequest(new {message = "Task should be in progress"});
                }
            }

            updateDto.CopyPropsTo(task);
            await _repository.Update(task);
            return NoContent();
        }

        [HttpDelete("{id}")]
        public async Task<IActionResult> DeleteById(Guid id)
        {
            var task = await _repository.GetById(id);
            if (task == null) return NotFound();
            await _repository.Delete(task);
            return NoContent();

        }
    }
}