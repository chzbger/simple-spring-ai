// simple-jwt-auth 라이브러리의 /sja/auth.js 가 노출하는 인스턴스 타입.

declare global {
  interface SimpleJwtAuthInstance {
    readonly isLoggedIn: boolean;
    readonly userId: string | null;
    readonly lastOAuthError: string | null;
    ready(): Promise<void>;
    login(username: string, password: string): Promise<void>;
    loginWithGoogle(): void;
    logout(): Promise<void>;
    fetch(url: string, options?: RequestInit): Promise<Response>;
    getAccessToken(): Promise<string | null>;
    addAuthListener(fn: (loggedIn: boolean) => void): () => void;
  }

  interface Window {
    auth: SimpleJwtAuthInstance;
  }
}

export {};
