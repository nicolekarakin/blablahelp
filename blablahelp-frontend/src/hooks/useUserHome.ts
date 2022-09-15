import axios from "axios";
import {urls} from "../shared/UrlMapping";
import userType, {UserType} from "../types/UserType";
import {useContext} from "react";
import {AuthContext} from "../shared/AuthProvider";
import {useSnackbar} from "notistack";
import {OwnInquiryResponseType} from "../types/OwnInquiryType";
import OwnOfferType from "../types/OwnOfferType";

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
                setCurrentUser((currentUser: UserType) => ({...currentUser, userData: data}))
            })
            .catch(_ => {
                enqueueSnackbar("Fetching user data failed!", {variant: "error"});
            });
    }

    const getUserOffers = () => {
        return axios.get(urls.BASIC[0] + urls.BASIC[2] + "/" + currentUser.id + urls.BASIC[1])
            .then(response => response.data)
            .then(data => {
                setCurrentUser((currentUser: UserType) => ({...currentUser, currentOffers: data}))
            })
            .catch(_ => {
                enqueueSnackbar("Fetching offer data for user with id " + currentUser.id + " failed!", {variant: "error"});
            });
    }

    const getUserInquiries = () => {
        return axios.get(urls.BASIC[0] + urls.BASIC[2] + "/" + currentUser.id + urls.BASIC[6])
            .then(response => response.data)
            .then(data => {
                setCurrentUser((currentUser: UserType) => ({...currentUser, currentInquiries: data}))
            })
            .catch(_ => {
                enqueueSnackbar("Fetching inquiry data for user with id " + currentUser.id + " failed!", {variant: "error"});
            });
    }

    const updateCurrentOffers = (ownOffer: OwnOfferType) => {
        const updatedOffers: OwnOfferType = currentUser.currentOffers.map((obj: OwnOfferType) => {
            if (obj.offerId === ownOffer.offerId) {
                return {...ownOffer};
            }
            return obj;
        });

        setCurrentUser((currentUser: userType) => ({
            ...currentUser, currentOffers: updatedOffers
        }));

    }

    const updateCurrentInquiries = (ownInquiry: OwnInquiryResponseType) => {
        const updatedInquiries: OwnInquiryResponseType[] = currentUser.currentInquiries.map((obj: OwnInquiryResponseType) => {
            if (obj.offer.offerId === ownInquiry.offer.offerId) {
                return {...ownInquiry};
            }
            return obj;
        });

        setCurrentUser((currentUser: userType) => ({
            ...currentUser, currentOffers: updatedInquiries
        }));

    }

    return {
        getUserOffers,
        getUserData,
        getPrivateHomeData,
        getUserInquiries,
        updateCurrentOffers,
        updateCurrentInquiries
    }

}