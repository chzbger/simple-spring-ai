import { useCallback, useEffect, useState } from 'react';
import { request } from '../api/graphqlClient';
import type { CurrentUser } from '../types';

const SIGNUP_MUTATION = `
  mutation Signup($input: SignupInput!) {
    signup(input: $input) { id username email }
  }
`;

const CURRENT_USER_QUERY = `
  query CurrentUser {
    currentUser { id username email }
  }
`;

type Status = 'loading' | 'authenticated' | 'anonymous';

export function useAuth() {
  const [status, setStatus] = useState<Status>('loading');
  const [user, setUser] = useState<CurrentUser | null>(null);

  useEffect(() => {
    let active = true;

    const handleChange = async (loggedIn: boolean) => {
      if (!active) return;
      if (!loggedIn) {
        setUser(null);
        setStatus('anonymous');
        return;
      }
      try {
        const data = await request<{ currentUser: CurrentUser | null }>(CURRENT_USER_QUERY);
        if (!active) return;
        setUser(data.currentUser);
        setStatus('authenticated');
      } catch (e) {
        console.error('[auth] currentUser failed:', e);
        if (!active) return;
        setUser(null);
        setStatus('anonymous');
      }
    };

    // addAuthListener 가 초기 restore 직후 1회 자동 발화하므로 별도 ready() await 불필요.
    const unsubscribe = window.auth.addAuthListener(handleChange);
    return () => {
      active = false;
      unsubscribe();
    };
  }, []);

  const login = useCallback(async (username: string, password: string) => {
    await window.auth.login(username, password);
    // login 성공 시 auth 내부에서 setAccessToken -> listener 발화 -> 위 handleChange 가 상태 갱신
  }, []);

  const signup = useCallback(async (username: string, password: string, email?: string) => {
    await request(SIGNUP_MUTATION, { input: { username, password, email: email ?? null } });
    await window.auth.login(username, password);
  }, []);

  const logout = useCallback(async () => {
    await window.auth.logout();
  }, []);

  return { status, user, login, signup, logout };
}
