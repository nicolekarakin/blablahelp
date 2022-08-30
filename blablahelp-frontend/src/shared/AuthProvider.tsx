import React, {createContext, ReactNode, useEffect, useState} from "react";
import UserType from "../types/UserType";

interface AuthProviderType {
    children: ReactNode;
}
type ValueProp = {
    currentUser: UserType | null,
    setCurrentUser: React.Dispatch<React.SetStateAction<UserType | null>>
}
const AuthContext = createContext<any | ValueProp>(null)

const getElementText=(response: Document, elementName: string)=>{
    return response.getElementsByTagName(elementName)[0].innerHTML;
}

function getIpAddress(currentLang:string, setCurrenCountry: (s: string) => void) {
    //TODO implement manual country switch
    fetch('http://api.hostip.info').then(response => {
        return response.text();
    }).then(xml => {
        return (new window.DOMParser()).parseFromString(xml, "text/xml");
    }).then(xmlDoc => {
        const countryCode = getElementText(xmlDoc , "countryAbbrev");
        setCurrenCountry(countryCode)
    });
}

const AuthProvider = ({children}: AuthProviderType) => {
    const [currentLang, setCurrenLang] = useState<string>("de");
    const [currentCountry, setCurrentCountry] = useState<string | null>(null);
    const [currentUser, setCurrentUser] = useState<UserType | null>(null);
    useEffect(()=>{
        getIpAddress(currentLang,setCurrentCountry)
    },[])
    return (
        <AuthContext.Provider value={{currentUser, setCurrentUser,currentCountry,currentLang}} >
            {children}
        </AuthContext.Provider>
    );
};

export {AuthContext, AuthProvider};
