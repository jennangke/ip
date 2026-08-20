# Botzilla User Guide

Botzilla is a command-line task management chatbot. This bot lets you add, list, and track 3 different types of tasks — todos, deadlines, and events — right from your terminal.
```
    ____        __        _ ____     
   / __ )____  / /_____  (_) / /___ _
  / __  / __ \/ __/_  / / / / / __ `/
 / /_/ / /_/ / /_  / /_/ / / / /_/ / 
/_____/\____/\__/ /___/_/_/_/\__,_/

```
## Features

- **Add todos** — simple tasks with just a description
- **Add deadlines** — tasks with a due date/time
- **Add events** — tasks with a start and end time
- **List tasks** — view all tasks with their type and completion status
- **Mark / unmark tasks** — track which tasks are done
- **Exit gracefully** — say `bye` to end the session

## Commands

| Command | Description | Example |
|---|---|---|
| `todo <description>` | Add a todo task | `todo read book` |
| `deadline <description> /by <date>` | Add a task with a deadline | `deadline return book /by Sunday` |
| `event <description> /from <start> /to <end>` | Add an event | `event project meeting /from Mon 2pm /to 4pm` |
| `list` | Show all tasks | `list` |
| `mark <task number>` | Mark a task as done | `mark 1` |
| `unmark <task number>` | Mark a task as not done | `unmark 1` |
| `bye` | Exit the program | `bye` |

## Task Types

Tasks are displayed with a type icon and completion status:

- `[T]` — Todo
- `[D]` — Deadline
- `[E]` — Event
- `[X]` — Marked as done
- `[ ]` — Not done

