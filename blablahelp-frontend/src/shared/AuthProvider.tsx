import {createContext, ReactNode, useState} from "react";
import UserType from "../types/UserType";

interface AuthProviderType {
    children: ReactNode;
}

const AuthContext = createContext<any>(null)

const AuthProvider = ({children}: AuthProviderType) => {
    const [currentUser, setCurrentUser] = useState<UserType | null>(null);
    return (
        <AuthContext.Provider value={{currentUser, setCurrentUser}}>
            {children}
        </AuthContext.Provider>
    );
};

export {AuthContext, AuthProvider};