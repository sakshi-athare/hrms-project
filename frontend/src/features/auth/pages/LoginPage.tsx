// src/pages/login/LoginPage.tsx
// FIX: LoginForm.tsx and LoginPage.tsx were two parallel login implementations.
//      Merged into one. LoginForm.tsx can be deleted.
import { useState } from "react";
import { useLogin } from "../hooks/useLogin";
const LoginPage = () => {
  const { mutate, isPending, isError, error } = useLogin();

  const [email,    setEmail]    = useState("");
  const [password, setPassword] = useState("");
  const [showPw,   setShowPw]   = useState(false);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (!email || !password) return;
    mutate({ email, password });
  };

  // Extract a readable error message from the axios error
  const errorMessage =
    (error as any)?.response?.data?.message ?? "Invalid email or password. Please try again.";

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#f0f2f5]">

      {/* Decorative rings */}
      <div className="absolute inset-0 overflow-hidden pointer-events-none">
        <div className="absolute -top-40 -left-40 w-150 h-150 rounded-full border border-[#3B6FF0]/10" />
        <div className="absolute -bottom-40 -right-40 w-125 h-125 rounded-full border border-[#3B6FF0]/8" />
      </div>

      <div className="w-full max-w-md px-4 relative">

        {/* Logo */}
        <div className="flex items-center justify-center gap-2.5 mb-8">
          <div className="w-10 h-10 rounded-xl bg-[#3B6FF0] flex items-center justify-center text-white">
            <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.2" className="w-5 h-5">
              <circle cx="12" cy="8" r="4" />
              <path d="M4 20c0-4 3.6-7 8-7s8 3 8 7" />
            </svg>
          </div>
          <div className="leading-tight">
            <p className="text-[17px] font-bold text-[#111827]">HRMS</p>
            <p className="text-[11px] text-[#9ca3af]">HR Management</p>
          </div>
        </div>

        {/* Card */}
        <div className="bg-white border border-[#e5e7eb] rounded-2xl shadow-[0_4px_24px_rgba(0,0,0,.07)] p-8">

          <div className="mb-6">
            <h1 className="text-xl font-bold text-[#111827]">Welcome back</h1>
            <p className="text-sm text-[#6b7280] mt-1">Sign in to your HRMS account</p>
          </div>

          <form onSubmit={handleSubmit} className="space-y-4">

            {/* Email */}
            <div className="space-y-1">
              <label className="block text-sm font-medium text-[#374151]">Email</label>
              <input
                type="email"
                placeholder="you@company.com"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                autoComplete="email"
                className="w-full px-3 py-2.5 text-sm border border-[#e5e7eb] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#3B6FF0]/30 focus:border-[#3B6FF0] placeholder:text-[#9ca3af]"
              />
            </div>

            {/* Password */}
            <div className="space-y-1">
              <label className="block text-sm font-medium text-[#374151]">Password</label>
              <div className="relative">
                <input
                  type={showPw ? "text" : "password"}
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  autoComplete="current-password"
                  className="w-full px-3 py-2.5 pr-10 text-sm border border-[#e5e7eb] rounded-lg focus:outline-none focus:ring-2 focus:ring-[#3B6FF0]/30 focus:border-[#3B6FF0] placeholder:text-[#9ca3af]"
                />
                <button
                  type="button"
                  onClick={() => setShowPw(!showPw)}
                  tabIndex={-1}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-[#9ca3af] hover:text-[#6b7280]"
                  aria-label={showPw ? "Hide password" : "Show password"}
                >
                  {showPw ? (
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-4 h-4">
                      <path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19m-6.72-1.07a3 3 0 11-4.24-4.24" />
                      <line x1="1" y1="1" x2="23" y2="23" />
                    </svg>
                  ) : (
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-4 h-4">
                      <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z" />
                      <circle cx="12" cy="12" r="3" />
                    </svg>
                  )}
                </button>
              </div>
            </div>

            {/* Error — shows the actual server message now */}
            {isError && (
              <div className="flex items-center gap-2 text-xs text-[#ef4444] bg-[#fee2e2] px-3 py-2 rounded-lg">
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" className="w-4 h-4 shrink-0">
                  <circle cx="12" cy="12" r="10" />
                  <line x1="12" y1="8" x2="12" y2="12" />
                  <line x1="12" y1="16" x2="12.01" y2="16" />
                </svg>
                {errorMessage}
              </div>
            )}

            {/* Submit */}
            <button
              type="submit"
              disabled={isPending || !email || !password}
              className="w-full bg-[#3B6FF0] text-white text-sm font-semibold py-2.5 rounded-lg hover:bg-[#2d5edf] transition active:scale-[.98] disabled:opacity-60 mt-2 inline-flex items-center justify-center gap-2"
            >
              {isPending ? (
                <>
                  <svg className="animate-spin w-4 h-4" viewBox="0 0 24 24" fill="none">
                    <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" />
                    <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v8z" />
                  </svg>
                  Signing in…
                </>
              ) : "Sign in"}
            </button>
          </form>
        </div>

        <p className="text-center text-xs text-[#9ca3af] mt-5">
          © {new Date().getFullYear()} HRMS HR Management
        </p>
      </div>
    </div>
  );
};

export default LoginPage;