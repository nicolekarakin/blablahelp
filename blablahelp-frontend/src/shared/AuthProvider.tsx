import React, {createContext, ReactNode, useState} from "react";
import UserType from "../types/UserType";

interface AuthProviderType {
    children: ReactNode;
}
 type ValueProp = {
    currentUser: UserType | null,
    setCurrentUser: React.Dispatch<React.SetStateAction<UserType | null>>
}
const AuthContext = createContext<any | ValueProp>(null)

const AuthProvider = ({children}: AuthProviderType) => {
    const [currentUser, setCurrentUser] = useState<UserType | null>(null);
    return (
        <AuthContext.Provider value={{currentUser, setCurrentUser}}>
            {children}
        </AuthContext.Provider>
    );
};

export {AuthContext, AuthProvider};
