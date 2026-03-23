/**
 * FitConnect - API Service
 * Centralized module for all backend API calls
 * Uses Fetch API with JWT authentication
 */

const API_BASE = 'http://localhost:8080/api';

// =============================================
// TOKEN MANAGEMENT
// =============================================
const Auth = {
  getToken: () => localStorage.getItem('fitconnect_token'),
  getUser:  () => JSON.parse(localStorage.getItem('fitconnect_user') || 'null'),
  isLoggedIn: () => !!localStorage.getItem('fitconnect_token'),

  setSession(token, user) {
    localStorage.setItem('fitconnect_token', token);
    localStorage.setItem('fitconnect_user', JSON.stringify(user));
  },

  clearSession() {
    localStorage.removeItem('fitconnect_token');
    localStorage.removeItem('fitconnect_user');
  },

  requireAuth() {
    if (!this.isLoggedIn()) {
      window.location.href = 'login.html';
      return false;
    }
    return true;
  }
};

// =============================================
// HTTP HELPER
// =============================================
async function request(method, endpoint, body = null, auth = true) {
  const headers = { 'Content-Type': 'application/json' };

  if (auth && Auth.getToken()) {
    headers['Authorization'] = `Bearer ${Auth.getToken()}`;
  }

  const config = { method, headers };
  if (body) config.body = JSON.stringify(body);

  try {
    const response = await fetch(`${API_BASE}${endpoint}`, config);
    const data = await response.json();

    // Token expired or unauthorized
    if (response.status === 401) {
      Auth.clearSession();
      window.location.href = 'login.html';
      throw new Error('Session expired. Please login again.');
    }

    if (!response.ok) {
      throw new Error(data.message || `Request failed: ${response.status}`);
    }

    return data;
  } catch (err) {
    if (err.message.includes('Failed to fetch')) {
      throw new Error('Cannot connect to server. Is the backend running?');
    }
    throw err;
  }
}

// =============================================
// AUTH API
// =============================================
const AuthAPI = {
  register: (data) => request('POST', '/auth/register', data, false),
  login:    (data) => request('POST', '/auth/login', data, false),
};

// =============================================
// TRAINER API
// =============================================
const TrainerAPI = {
  getAll:   ()     => request('GET', '/trainers'),
  getById:  (id)   => request('GET', `/trainers/${id}`),
  getMyProfile: () => request('GET', '/trainers/my-profile'),

  search: (params) => {
    const q = new URLSearchParams();
    if (params.location) q.append('location', params.location);
    if (params.skill)    q.append('skill', params.skill);
    if (params.maxRate)  q.append('maxRate', params.maxRate);
    if (params.minExp)   q.append('minExp', params.minExp);
    return request('GET', `/trainers/search?${q.toString()}`);
  },

  createProfile: (data) => request('POST', '/trainers/profile', data),
  updateProfile: (data) => request('PUT',  '/trainers/profile', data),
  delete:        (id)   => request('DELETE', `/trainers/${id}`),
};

// =============================================
// BOOKING API
// =============================================
const BookingAPI = {
  create:         (data) => request('POST', '/bookings', data),
  getMyBookings:  ()     => request('GET', '/bookings/my-bookings'),
  getTrainerBookings: () => request('GET', '/bookings/trainer-bookings'),
  updateStatus:   (id, status) => request('PUT', `/bookings/${id}/status?status=${status}`),
  cancel:         (id)   => request('PUT', `/bookings/${id}/cancel`),
};

// =============================================
// REVIEW API
// =============================================
const ReviewAPI = {
  getForTrainer: (trainerId) => request('GET', `/reviews/trainer/${trainerId}`, null, false),
  create:        (data)      => request('POST', '/reviews', data),
  delete:        (id)        => request('DELETE', `/reviews/${id}`),
};

// =============================================
// UI HELPERS
// =============================================

/** Show toast notification */
function showToast(message, type = 'info') {
  const container = document.getElementById('toast-container') ||
    (() => {
      const el = document.createElement('div');
      el.id = 'toast-container';
      document.body.appendChild(el);
      return el;
    })();

  const icons = { success: '✅', error: '❌', info: 'ℹ️', warning: '⚠️' };
  const toast = document.createElement('div');
  toast.className = `toast ${type}`;
  toast.innerHTML = `<span>${icons[type] || icons.info}</span><span>${message}</span>`;
  container.appendChild(toast);

  setTimeout(() => {
    toast.style.animation = 'slideInRight 0.3s ease reverse';
    setTimeout(() => toast.remove(), 300);
  }, 3500);
}

/** Render star rating HTML */
function renderStars(rating) {
  const full = Math.round(rating);
  return '★'.repeat(full) + '☆'.repeat(5 - full);
}

/** Get initials from name */
function getInitials(name) {
  return name ? name.split(' ').map(n => n[0]).join('').toUpperCase().slice(0, 2) : '?';
}

/** Format date */
function formatDate(dateStr) {
  return new Date(dateStr).toLocaleDateString('en-IN', {
    day: 'numeric', month: 'short', year: 'numeric'
  });
}

/** Get booking status badge HTML */
function statusBadge(status) {
  const map = {
    PENDING:   ['badge-warning', '⏳ Pending'],
    ACCEPTED:  ['badge-success', '✅ Accepted'],
    REJECTED:  ['badge-danger',  '❌ Rejected'],
    COMPLETED: ['badge-info',    '🏁 Completed'],
    CANCELLED: ['badge-danger',  '🚫 Cancelled'],
  };
  const [cls, label] = map[status] || ['badge-info', status];
  return `<span class="badge ${cls}">${label}</span>`;
}

/** Populate navbar with user info */
function initNavbar() {
  const user = Auth.getUser();
  const navUser = document.getElementById('nav-user');
  const navLinks = document.getElementById('nav-links');

  if (!user) {
    if (navUser) navUser.innerHTML = `
      <a href="login.html" class="btn btn-outline btn-sm">Login</a>
      <a href="signup.html" class="btn btn-primary btn-sm">Sign Up</a>`;
    return;
  }

  if (navUser) {
    navUser.innerHTML = `
      <div class="nav-avatar" title="${user.name}">${getInitials(user.name)}</div>
      <span style="font-size:0.85rem;color:var(--text-muted)">${user.name}</span>
      ${user.role === 'ADMIN' ? '<a href="admin.html" class="btn btn-sm" style="background:#ff6b35;color:white;border:none">Admin Panel</a>' : ''}
      <button class="btn btn-secondary btn-sm" onclick="logout()">Logout</button>`;
  }

  // Show trainer-specific links
  if (navLinks && user.role === 'TRAINER') {
    const trainerLink = document.getElementById('nav-trainer-link');
    if (trainerLink) trainerLink.style.display = 'block';
  }
}

function logout() {
  Auth.clearSession();
  showToast('Logged out successfully', 'info');
  setTimeout(() => window.location.href = 'login.html', 1000);

}

// Init navbar on every page
document.addEventListener('DOMContentLoaded', initNavbar);
