using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using dmd_test.Models;
using dmd_test.Repositories;
using dmd_test.ViewModels;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.HttpsPolicy;
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;
using Microsoft.Extensions.Logging;
using Microsoft.OpenApi.Models;

namespace dmd_test
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.Configure<JsonOptions>(opts =>
            {
                opts.JsonSerializerOptions.IgnoreNullValues = true;
            });
            services.AddControllers();
            services.AddDbContext<TaskDbSet>(opts => opts.UseNpgsql(Configuration.GetConnectionString("TaskConnection")));
            services.AddScoped<ITaskRepository, TaskRepository>();
            services.AddSwaggerGen(c => { c.SwaggerDoc("v1", new OpenApiInfo {Title = "dmd_test", Version = "v1"}); });
            services.AddCors(options =>
                options.AddDefaultPolicy(builder =>
                {
                    builder.WithOrigins("https://localhost:5003", "http:localhost:5002").AllowAnyMethod();
                }));
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
                app.UseSwagger();
                app.UseSwaggerUI(c => c.SwaggerEndpoint("/swagger/v1/swagger.json", "dmd_test v1"));
            }

            app.UseHttpsRedirection();

            app.UseRouting();
            app.UseCors();
            app.UseAuthorization();

            app.UseEndpoints(endpoints => { endpoints.MapControllers(); });
        }
    }
}