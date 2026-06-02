import { Component, inject } from '@angular/core';
import { ThemeService } from '../../core/services/theme.service';

@Component({
  selector: 'app-theme-toggle',
  standalone: true,
  template: `
    <button
      type="button"
      class="theme-toggle"
      (click)="theme.toggle()"
      [attr.aria-label]="theme.isDark() ? 'Modo claro' : 'Modo oscuro'"
      [title]="theme.isDark() ? 'Cambiar a modo claro' : 'Cambiar a modo oscuro'"
    >
      <span class="material-icons theme-toggle__icon">
        {{ theme.isDark() ? 'light_mode' : 'dark_mode' }}
      </span>
    </button>
  `,
  styles: [`
    .theme-toggle {
      width: 48px;
      height: 48px;
      border: none;
      border-radius: 14px;
      cursor: pointer;
      display: flex;
      align-items: center;
      justify-content: center;
      background: linear-gradient(145deg, var(--theme-btn-from), var(--theme-btn-to));
      color: #fff;
      box-shadow: 0 4px 18px var(--theme-btn-shadow);
      transition: transform 0.2s ease, box-shadow 0.2s ease;

      &:hover {
        transform: scale(1.06);
        box-shadow: 0 6px 22px var(--theme-btn-shadow);
      }

      &:active {
        transform: scale(0.98);
      }
    }

    .theme-toggle__icon {
      font-size: 28px;
    }
  `]
})
export class ThemeToggleComponent {
  readonly theme = inject(ThemeService);
}
