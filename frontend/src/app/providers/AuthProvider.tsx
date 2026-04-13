import { useEffect } from "react";
import { useAuthStore } from "../../features/auth/model/authStore";
import { getMe } from "../../features/auth/api/getMe";

const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const { setUser, setLoading } = useAuthStore();

  useEffect(() => {
  const init = async () => {
    try {
      const me = await getMe();
      console.log("ME USER:", me); // 🔍 debug
      setUser(me);
    } catch (err) {
      console.log("getMe failed", err);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  init();
}, []);

  return <>{children}</>;
};

export default AuthProvider;