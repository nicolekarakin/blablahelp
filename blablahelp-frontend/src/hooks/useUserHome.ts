import axios from "axios";
import {urls} from "../shared/UrlMapping";
import {UserDataType} from "../types/UserType";
import {useContext} from "react";
import {AuthContext} from "../shared/AuthProvider";
import {useSnackbar} from "notistack";

export default function useUserHome() {

    const {currentUser, setCurrentUser} = useContext(AuthContext);
    const {enqueueSnackbar} = useSnackbar();

    const getPrivateHomeData = (setUserMessage: React.Dispatch<React.SetStateAction<string | undefined>>) => {
        return axios.get(urls.BASIC[0])
            .then(response => response.data)
            .then(data => setUserMessage(data))
            .catch(_ => {
                enqueueSnackbar("Fetching private home data for user failed!", {variant: "error"});
            });
    }

    const getUserData = () => {
        return axios.get(urls.BASIC[0] + urls.BASIC[2] + "/" + currentUser.id)
            .then(response => response.data)
            .then(data => {
                currentUser.userData = data;
                setCurrentUser({...currentUser})
            })
            .catch(_ => {
                enqueueSnackbar("Fetching user data failed!", {variant: "error"});
            });
    }

    const getUserOffers = () => {
        return axios.get(urls.BASIC[0] + urls.BASIC[2] + "/" + currentUser.id + urls.BASIC[1])
            .then(response => response.data)
            .then(data => {
                const userDataUpdated: UserDataType = {...currentUser.userData, currentOffers: data}
                setCurrentUser({...currentUser, userData: userDataUpdated})
            })
            .catch(_ => {
                enqueueSnackbar("Fetching offer data for user with id " + currentUser.id + " failed!", {variant: "error"});
            });
    }

    return {
        getUserOffers,
        getUserData,
        getPrivateHomeData,
    }

}