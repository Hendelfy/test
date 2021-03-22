using System;
using Microsoft.EntityFrameworkCore.Migrations;

namespace dmd_test.Migrations
{
    public partial class RemoveTotalValues : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "ExecutionTimeTotal",
                table: "Tasks");

            migrationBuilder.DropColumn(
                name: "PlannedDifficultyTotal",
                table: "Tasks");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<DateTime>(
                name: "ExecutionTimeTotal",
                table: "Tasks",
                type: "timestamp without time zone",
                nullable: true);

            migrationBuilder.AddColumn<int>(
                name: "PlannedDifficultyTotal",
                table: "Tasks",
                type: "integer",
                nullable: true);
        }
    }
}
