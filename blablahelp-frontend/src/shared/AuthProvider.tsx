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

function getIpCountry(setCurrenCountry: (s: string) => void) {
    fetch('https://api.hostip.info/country.php')
        .then(response => {
            return response.text();
        }).then(str => {
        console.log(str)
        setCurrenCountry(str)
    });
}

const AuthProvider = ({children}: AuthProviderType) => {
    const [currentLang, setCurrenLang] = useState<string>("de");
    const [currentCountry, setCurrentCountry] = useState<string>("DE");
    const [currentUser, setCurrentUser] = useState<UserType | null>(null);

    //TODO(@nicolekarakin) implement manual country and language switch
    //TODO(@nicolekarakin) call getIpCountry(setCurrentCountry) in useEffect

    return (
        <AuthContext.Provider value={{currentUser, setCurrentUser, currentCountry, currentLang}}>
            {children}
        </AuthContext.Provider>
    );
};

export {AuthContext, AuthProvider};
