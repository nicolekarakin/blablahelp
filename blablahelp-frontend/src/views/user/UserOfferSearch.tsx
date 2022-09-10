import {
    Button,
    Card,
    CardActions,
    CardContent,
    CardHeader,
    CircularProgress,
    FormControl,
    FormControlLabel,
    FormLabel,
    Grid,
    InputLabel,
    MenuItem,
    Radio,
    RadioGroup,
    Select,
    Stack,
    TextField,
    Typography
} from "@mui/material";

import React, {FormEvent, useContext, useEffect, useState} from "react";
import {useNavigate} from "react-router";
import {AuthContext} from "../../shared/AuthProvider";
import AddressType, {AddressWrapType} from "../../types/AddressType";
import axios from "axios";
import {urls} from "../../shared/UrlMapping";
import {useSnackbar} from "notistack";
import MatchedOffers from "../../components/user/match/MatchedOffers";
import NotOwnOfferType from "../../types/NotOwnOfferType";
import useUserHome from "../../hooks/useUserHome";

export default function UserOfferSearch() {
    const {currentUser, currentCountry} = useContext(AuthContext);
    const [loading, setLoading] = useState(false);
    const [useSaved, setUseSaved] = useState<string | undefined>("true");
    const {getUserData} = useUserHome()
    const [mitshopperAddress, setMitshopperAddress] = useState<AddressType>({
        city: "", country: currentCountry, street: "", zip: ""
    });
    const [matchingOffers, setMatchingOffers] = useState<NotOwnOfferType[]>([])
    const navigate = useNavigate()
    const {enqueueSnackbar} = useSnackbar();

    useEffect(() => {
        if (!currentUser) navigate("/login")
    }, []);


    const getMatchedOffers = (address: AddressType) => {
        const addressWith = {accountId: currentUser.id, address: address, firstname: currentUser.firstname}

        return axios.post(urls.BASIC[0] + urls.BASIC[2] + urls.BASIC[4], addressWith)
            .then(response => response.data)
            .then(data => {
                setMatchingOffers([...data])
            })
            .catch(_ => {
                enqueueSnackbar("Fetching search data failed!", {variant: "error"});
            });
    }

    const handleSubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        setLoading(true);
        getMatchedOffers(mitshopperAddress)
            .then(() => {
                if (useSaved === "false") getUserData()
            })
            .finally(() => {
                setLoading(false)
            });
    }

    const getUsedPrivateAddresses = () => {
        if (currentUser && currentUser.userData && currentUser.userData.usedAddresses)
            return currentUser.userData.usedAddresses
                .filter((a: AddressWrapType) => a.type === "PRIVATE")
        else return []
    }

    return (
        <>
            <Card elevation={3}>
                <form onSubmit={handleSubmit}>
                    <CardHeader title={"Nach einem Angebot suchen"}/>
                    <CardContent>

                        <Stack spacing={2}>
                            <Typography component={'p'} sx={{fontSize: "1.2rem"}}>Lieferungsadresse</Typography>

                            {
                                (!!(currentUser?.userData)
                                    && !!(currentUser?.userData.usedAddresses)
                                    && getUsedPrivateAddresses().length > 0) ?
                                    <FormControl>
                                        <FormLabel id="useSaved-radio-buttons-group-label">
                                            Gespeicherte oder neue Adresse</FormLabel>
                                        <RadioGroup aria-labelledby="useSaved-radio-buttons-group-label" row
                                                    value={useSaved ?? ""}
                                                    onChange={(e) => {
                                                        setMitshopperAddress({
                                                            city: "",
                                                            country: currentCountry,
                                                            street: "",
                                                            zip: ""
                                                        })
                                                        setUseSaved(e.target.value)
                                                    }}
                                        >

                                            <FormControlLabel value={"true"} control={<Radio/>}
                                                              label="Gespeicherte Adressen"/>
                                            <FormControlLabel value={"false"} control={<Radio/>} label="Neue Adresse"/>
                                        </RadioGroup>
                                    </FormControl>
                                    : <></>
                            }

                            {
                                (useSaved === "true" && getUsedPrivateAddresses().length > 0) ?

                                    <FormControl fullWidth>
                                        <InputLabel id={"usedBefore"}>Gespeicherte Adressen</InputLabel>
                                        <Select
                                            required
                                            labelId="usedBefore"
                                            value={(!!mitshopperAddress.zip) ? JSON.stringify(mitshopperAddress) : ""}
                                            label="Gespeicherte Adressen"

                                            onChange={(e) => {
                                                setMitshopperAddress(JSON.parse(e.target.value))
                                            }}
                                        >
                                            {
                                                getUsedPrivateAddresses()
                                                    .map((addressWrap: AddressWrapType, index: React.Key | null | undefined) =>
                                                        <MenuItem value={JSON.stringify(addressWrap.address)}
                                                                  key={index}>
                                                            {addressWrap.address.street + ", " + addressWrap.address.zip + " " + addressWrap.address.city}
                                                        </MenuItem>
                                                    )
                                            }
                                        </Select>
                                    </FormControl>
                                    :
                                    <Grid container>
                                        <Grid item md={8} xs={12} sx={{paddingLeft: "1rem", paddingBottom: "1.2rem"}}>
                                            <TextField
                                                fullWidth
                                                required
                                                variant="outlined"
                                                placeholder={"Stadt"}
                                                value={mitshopperAddress?.city ?? ""}
                                                onChange={e => {
                                                    setMitshopperAddress({...mitshopperAddress, city: e.target.value})
                                                }
                                                }
                                                label="Stadt"
                                                sx={{
                                                    "& .Mui-focused .MuiIconButton-root": {color: "primary.main"}
                                                }}
                                            />
                                        </Grid>
                                        <Grid item md={4} xs={12} sx={{paddingLeft: "1rem", paddingBottom: "1.2rem"}}>
                                            <TextField
                                                fullWidth
                                                required
                                                variant="outlined"
                                                placeholder={"Postleitzahl"}
                                                value={mitshopperAddress?.zip ?? ""}
                                                onChange={e => {
                                                    setMitshopperAddress({...mitshopperAddress, zip: e.target.value})
                                                }
                                                }
                                                label="Postleitzahl"
                                                sx={{
                                                    "& .Mui-focused .MuiIconButton-root": {color: "primary.main"}
                                                }}
                                            />
                                        </Grid>
                                        <Grid item md={12} xs={12} sx={{paddingLeft: "1rem"}}>
                                            <TextField
                                                fullWidth
                                                required
                                                variant="outlined"
                                                placeholder={"Straße und Hausnummer"}
                                                value={mitshopperAddress?.street ?? ""}
                                                onChange={e => {
                                                    setMitshopperAddress({...mitshopperAddress, street: e.target.value})
                                                }
                                                }
                                                label="Straße und Hausnummer"
                                                sx={{
                                                    "& .Mui-focused .MuiIconButton-root": {color: "primary.main"}
                                                }}
                                            />
                                        </Grid>
                                    </Grid>
                            }

                            {/*<DynamicTextInput*/}
                            {/*    label={"Shop Brand"}*/}
                            {/*    required={true}*/}
                            {/*    disabled={false}*/}
                            {/*    getData={getShopNames}*/}
                            {/*    helperText=""*/}
                            {/*    axiosProps={{}}*/}
                            {/*    noOptionsText={"Leider keine Option verfügbar"}*/}
                            {/*    setSelectValue={setShopname}*/}
                            {/*    selectValue={shopname}*/}
                            {/*/>*/}
                        </Stack>

                    </CardContent>
                    <CardActions sx={{padding: '0 1rem 1.5rem 1rem'}}>
                        <Button variant={"contained"}
                                color={"primary"}
                                type="submit"
                        >Suchen</Button>

                        <Button
                            variant={"outlined"}
                            onClick={_ => navigate("/account")}
                        >Zurück</Button>
                    </CardActions>
                </form>
            </Card>

            {loading && (
                <CircularProgress/>
            )}
            {
                (!loading && (!!matchingOffers && matchingOffers.length > 0)) &&
                <MatchedOffers offers={matchingOffers} mitshopperAddress={mitshopperAddress}/>
            }

        </>
    )
}