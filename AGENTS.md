# Repository Instructions

This file captures project-specific implementation rules for humans and AI coding agents working in this repository.

## UI Action Feedback Rule

Every user-triggered action must provide clear feedback while it is running and after it completes.

Apply this rule to any screen, button, menu action, toolbar action, modal action, upload, save, refresh, delete, sync, import, export, submit, or generate workflow.

### Required behavior

1. Show an immediate in-progress state.
2. Prevent duplicate submissions while the action is running.
3. Show a clear success or failure result.
4. Do not leave the user guessing whether the system received the action.

### Implementation guidance

- Use a local action state such as `idle`, `dirty`, `loading`, `saving`, `success`, or `error` where it helps.
- Disable the triggering control while the request is in flight, or otherwise guard against repeated clicks.
- Update button labels or icons during execution when the action is important enough to deserve direct feedback.
- Prefer both inline status and toast feedback for important workflows such as save, submit, sync, import, export, and delete.
- Surface meaningful error text when available instead of relying only on generic interceptor messages.
- For long-running actions, keep a visible status on screen until the result is clear.
- For destructive actions, keep confirmation and post-action result feedback.

### Minimum standard for any new action

- Before request: control is enabled and clearly named.
- During request: visible working state appears immediately.
- During request: duplicate clicks are blocked.
- After success: user can tell the action completed.
- After failure: user can tell the action failed and what to do next.

## Warehouse Optimize Note

The Warehouse Optimize design screen is the current reference implementation for this interaction pattern. Reuse that behavior when building similar workflows elsewhere in the app.
