import React, {FormEvent, useContext, useEffect, useState} from "react";
import {AuthContext} from "../../shared/AuthProvider";
import {CloseSharp as ClearIcon} from "@mui/icons-material";
import {
    Button,
    Card,
    CardActions,
    CardContent,
    CardHeader,
    FormControl,
    FormControlLabel,
    FormLabel,
    Grid,
    IconButton,
    InputLabel,
    MenuItem,
    Radio,
    RadioGroup,
    Select,
    Stack,
    TextField,
    Typography
} from "@mui/material";

import {useSnackbar} from "notistack";
import OwnOfferType from "../../types/OwnOfferType";
import AddressType, {AddressWrapType} from "../../types/AddressType";
import DateTimeOfferFormPart from "../../components/user/DateTimeOfferFormPart";

import DynamicTextInput from "../../components/user/DynamicTextInput";
import useOfferForm from "../../hooks/useOfferForm";
import ConfirmationNewOfferDialog from "../../components/user/ConfirmationNewOfferDialog";
import {useNavigate} from "react-router";
import {urls} from "../../shared/UrlMapping";
import axios from "axios";
import userType, {UserDataType} from "../../types/UserType";


export default function OfferForm() {
    const {currentUser, setCurrentUser, currentCountry} = useContext(AuthContext);
    const [open, setOpen] = React.useState(false);

    const {getShopNames, getShopAddresses} = useOfferForm()
    const [shoppingDate, setShoppingDate] = useState<Date | null>(null);
    const [timeFrom, setTimeFrom] = useState<Date | null>(null);
    const [timeTo, setTimeTo] = useState<Date | null>(null);

    const [shopCity, setShopCity] = useState<string | undefined>(undefined);
    const [shopname, setShopname] = useState<string | undefined>(undefined);

    const [shopAddress, setShopAddress] = useState<AddressType | null>(null);
    const [destinationAddress, setDestinationAddress] = useState<AddressType>({
        city: "", country: currentCountry, street: "", zip: ""
    });
    const [useSaved, setUseSaved] = useState<string | undefined>("true");

    const [maxMitshoppers, setMaxMitshoppers] = useState<number>(1);
    const [maxDrinksInLiter, setMaxDrinksInLiter] = useState<number>(1);
    const [maxArticles, setMaxArticles] = useState<number>(10);
    const [maxDistanceKm, setMaxDistanceKm] = useState<number>(1);

    const [notes, setNotes] = useState<string | undefined>(undefined);
    const [priceOffer, setPriceOffer] = useState<number>(0);

    const [newOfferData,setNewOfferData]=useState<OwnOfferType>();
    const {enqueueSnackbar} = useSnackbar();

    const navigate=useNavigate()

    useEffect(() => {
        if (!currentUser) navigate("/login")
    }, []);

    const handleDummySubmit = (e: FormEvent<HTMLFormElement>) => {
        e.preventDefault()
        const offerData: OwnOfferType = {
            offerId: undefined,
            accountId: currentUser.id,
            shoppingDay: shoppingDate?.getTime(),
            timeFrom: timeFrom?.getTime(),
            timeTo: timeTo?.getTime(),
            shopname: shopname,
            shopAddress: shopAddress,

            destinationAddress: destinationAddress,
            maxMitshoppers: maxMitshoppers,
            maxLiter: maxDrinksInLiter,
            maxArticles: maxArticles,
            maxDistanceKm: maxDistanceKm,
            notes:notes,
            priceOffer: priceOffer.toString(),
        }
        setNewOfferData(offerData)
        setOpen(true);
    };

    const handleClose = (value:boolean) => {
        setOpen(false);
        if (value) {
            handleNewOfferSubmit()
        }
    };

    const handleNewOfferSubmit = () => {
        console.debug("agreed and submiting!!");
        axios
            .post(urls.BASIC[0]+urls.BASIC[2]+"/"+currentUser.id+urls.BASIC[3], newOfferData)
            .then(response => {
                const ownOfferData:OwnOfferType = response?.data
                const updatedOffers:OwnOfferType[]=[...currentUser?.userData?.currentOffers ?? [], ownOfferData]
                const updatedUserData:UserDataType={...currentUser?.userData, currentOffers:updatedOffers}
                setCurrentUser((currentUser:userType) => ({
                    ...currentUser, userData: updatedUserData
                }));

            })
            .catch(_ => {
                enqueueSnackbar('New Offer Submission Failed', {variant: "error"})
            });
        navigate("/account")
    }

    const getUsedPrivateAddresses = () => {
        if(currentUser && currentUser.userData && currentUser.userData.usedAddresses)
            return currentUser.userData.usedAddresses
                .filter((a: AddressWrapType) => a.type === "PRIVATE")
        else return []
    }

    return (

        <Card elevation={3}>
            <form onSubmit={handleDummySubmit}>
                <CardHeader title={"Neues Angebot erstellen"}/>
                <CardContent>

                    <Stack spacing={2}>

                        <DynamicTextInput
                            label={"Shop Brand"}
                            required={true}
                            disabled={false}
                            getData={getShopNames}
                            helperText=""
                            axiosProps={{}}
                            noOptionsText={"Leider keine Option verfügbar"}
                            setSelectValue={setShopname}
                            selectValue={shopname}
                        />
                        <TextField
                            required
                            variant="outlined"
                            placeholder={"Deine Stadt"}
                            value={shopCity ?? ""}
                            onChange={e => setShopCity(e.target.value)}
                            fullWidth
                            error={false}
                            label="Stadt wo du einkaufst"
                            helperText=""
                            sx={{
                                "& .Mui-focused .MuiIconButton-root": { color: "primary.main" }
                            }}
                            InputProps={{
                                endAdornment: (
                                    <IconButton
                                        sx={{ visibility: shopCity ? "visible" : "hidden" }}
                                        onClick={()=>setShopCity(undefined)}
                                    >
                                        <ClearIcon />
                                    </IconButton>
                                ),
                            }}
                        />
                        {
                            ((!!shopname && shopname.length > 0) &&
                                (!!shopCity && shopCity.length > 0)) ?
                                <DynamicTextInput
                                    label={"Shop Adresse"}
                                    required={true}
                                    disabled={false}
                                    getData={getShopAddresses}
                                    setSelectValue={setShopAddress}
                                    selectValue={shopAddress}
                                    helperText=""
                                    axiosProps={{city: shopCity, shopname: shopname}}
                                    noOptionsText={"Leider kein Shop mit dieser Adresse vorhanden"}
                                />
                                : <></>
                        }

                        <Typography component={'p'} sx={{fontSize: "1.2rem"}}>Endzieladresse</Typography>

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
                                                        setDestinationAddress({
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
                                :
                                <></>
                        }

                        {
                            (useSaved === "true" && getUsedPrivateAddresses().length > 0) ?

                                <FormControl fullWidth>
                                    <InputLabel id={"usedBefore"}>Gespeicherte Adressen</InputLabel>
                                    <Select
                                        required
                                        labelId="usedBefore"
                                        value={(!!destinationAddress.zip)?JSON.stringify(destinationAddress): ""}
                                        label="Gespeicherte Adressen"

                                        onChange={(e) => {
                                            setDestinationAddress(JSON.parse(e.target.value))
                                        }}
                                    >
                                        {
                                            getUsedPrivateAddresses()
                                                .map((addressWrap: AddressWrapType, index: React.Key | null | undefined) =>
                                                    <MenuItem value={JSON.stringify(addressWrap.address)} key={index}>
                                                         {addressWrap.address.street + ", " + addressWrap.address.zip + " " + addressWrap.address.city}
                                                    </MenuItem>
                                                )
                                        }
                                    </Select>
                                </FormControl>
                                :
                                <Grid container >
                                    <Grid item md={8} xs={12} sx={{paddingLeft:"1rem",paddingBottom:"1.2rem"}}>
                                        <TextField
                                            fullWidth
                                            required
                                            variant="outlined"
                                            placeholder={"Stadt"}
                                            value={destinationAddress?.city ?? ""}
                                            onChange={e => {
                                                setDestinationAddress({...destinationAddress, city: e.target.value})
                                            }
                                            }
                                            label="Stadt"
                                            sx={{
                                                "& .Mui-focused .MuiIconButton-root": { color: "primary.main" }
                                            }}
                                            InputProps={{
                                                endAdornment: (
                                                    <IconButton
                                                        sx={{ visibility: shopCity ? "visible" : "hidden" }}
                                                        onClick={()=>setShopCity(undefined)}
                                                    >
                                                        <ClearIcon />
                                                    </IconButton>
                                                ),
                                            }}
                                        />
                                    </Grid>
                                    <Grid item md={4} xs={12} sx={{paddingLeft:"1rem",paddingBottom:"1.2rem"}}>
                                        <TextField
                                            fullWidth
                                            required
                                            variant="outlined"
                                            placeholder={"Postleitzahl"}
                                            value={destinationAddress?.zip ?? ""}
                                            onChange={e => {
                                                setDestinationAddress({...destinationAddress, zip: e.target.value})
                                            }
                                            }
                                            label="Postleitzahl"
                                            sx={{
                                                "& .Mui-focused .MuiIconButton-root": { color: "primary.main" }
                                            }}
                                            InputProps={{
                                                endAdornment: (
                                                    <IconButton
                                                        sx={{ visibility: shopCity ? "visible" : "hidden" }}
                                                        onClick={()=>setShopCity(undefined)}
                                                    >
                                                        <ClearIcon />
                                                    </IconButton>
                                                ),
                                            }}
                                        />
                                    </Grid>
                                    <Grid item md={12} xs={12} sx={{paddingLeft:"1rem"}}>
                                        <TextField
                                            fullWidth
                                            required
                                            variant="outlined"
                                            placeholder={"Straße und Hausnummer"}
                                            value={destinationAddress?.street ?? ""}
                                            onChange={e => {
                                                setDestinationAddress({...destinationAddress, street: e.target.value})
                                            }
                                            }
                                            label="Straße und Hausnummer"
                                            sx={{
                                                "& .Mui-focused .MuiIconButton-root": { color: "primary.main" }
                                            }}
                                            InputProps={{
                                                endAdornment: (
                                                    <IconButton
                                                        sx={{ visibility: shopCity ? "visible" : "hidden" }}
                                                        onClick={()=>setShopCity(undefined)}
                                                    >
                                                        <ClearIcon />
                                                    </IconButton>
                                                ),
                                            }}
                                        />
                                    </Grid>
                                </Grid>
                        }

                        <Typography component={'span'} sx={{fontSize: "1.2rem"}}>Maximale Distance Breite (1 – 20 km)</Typography>
                        <TextField
                            required
                            type={"number"}
                            variant="outlined"
                            value={maxDistanceKm ?? ""}
                            onChange={e => {
                                const valStr = (e.target as HTMLInputElement).value;
                                if (valStr !== "") {
                                    let val = parseInt(valStr);
                                    if (val > 20) val = 20;
                                    if (val < 1 || isNaN(val)) val = 1;
                                    setMaxDistanceKm(val)
                                }
                            }}
                            fullWidth
                            InputProps={{
                                inputProps: {
                                    max: 20, min: 1
                                }
                            }}
                        />
                        <Typography component={'span'} sx={{fontSize: "1.2rem"}}>Zeit und Datum</Typography>
                        <DateTimeOfferFormPart
                            setShoppingDate={setShoppingDate}
                            shoppingDate={shoppingDate}
                            setTimeFrom={setTimeFrom}
                            timeFrom={timeFrom}
                            setTimeTo={setTimeTo}
                            timeTo={timeTo}
                        />

                        <FormControl>
                            <FormLabel id=",maxMitshopper-radio-buttons-group-label">
                                Maximale Anzahl an Mitshoppern</FormLabel>
                            <RadioGroup aria-labelledby="maxMitshopper-radio-buttons-group-label" row
                                        value={maxMitshoppers ?? ""}
                                        onChange={(e) => {
                                            const val = (e.target as HTMLInputElement).value;
                                            setMaxMitshoppers(Number(val))
                                        }}
                            >
                                <FormControlLabel value={"1"} control={<Radio/>} label="Eins"/>
                                <FormControlLabel value={"2"} control={<Radio/>} label="Zwei"/>
                                <FormControlLabel value={"3"} control={<Radio/>} label="Drei"/>
                            </RadioGroup>
                        </FormControl>

                        <Typography component={'span'} sx={{fontSize: "1.2rem"}}>
                            Höchstzahl für Flüssigkeiten in Liter (1 – 20 l)</Typography>
                        <TextField
                            required
                            type={"number"}
                            variant="outlined"
                            value={maxDrinksInLiter ?? ""}
                            onChange={e => {
                                const valStr = (e.target as HTMLInputElement).value;
                                if (valStr !== "") {
                                    let val = parseInt(valStr);
                                    if (val > 20) val = 20;
                                    if (val < 1 || isNaN(val)) val = 1;
                                    setMaxDrinksInLiter(val)
                                }
                            }}
                            fullWidth
                            InputProps={{
                                inputProps: {
                                    max: 20, min: 1
                                }
                            }}
                        />

                        <Typography component={'span'} sx={{fontSize: "1.2rem"}}>
                            Maximale Anzahl für Produktartikel (ohne Flüssigkeiten) (10 – 60)</Typography>
                        <TextField
                            required
                            type={"number"}
                            variant="outlined"
                            value={maxArticles ?? ""}
                            onChange={e => {
                                const valStr = (e.target as HTMLInputElement).value;
                                if (valStr !== "") {
                                    let val = parseInt(valStr);
                                    if (val > 60) val = 60;
                                    if (val < 10 || isNaN(val)) val = 10;
                                    setMaxArticles(val)
                                }
                            }}
                            fullWidth
                            InputProps={{
                                inputProps: {
                                    max: 60, min: 10
                                }
                            }}
                        />

                        <TextField
                            label="Anmerkungen"
                            multiline
                            maxRows={10}
                            value={notes}
                            onChange={e=>setNotes(e.target.value)}

                        />

                        <Typography component={'span'} sx={{fontSize: "1.2rem"}}>
                            Preis Ihres Serviceangebots in Euro (0 – 60)</Typography>
                        <TextField
                            required
                            type={"number"}
                            variant="outlined"
                            value={priceOffer ?? ""}
                            onChange={e => {
                                const valStr = (e.target as HTMLInputElement).value;
                                if (valStr !== "") {
                                    let val = parseInt(valStr);
                                    if (val > 60) val = 60;
                                    if (val < 0 ||  isNaN(val)) val = 0;
                                    setPriceOffer(val)
                                }
                            }}
                            fullWidth
                            InputProps={{
                                inputProps: {
                                    max: 60, min: 0
                                }
                            }}
                        />

                    </Stack>

                </CardContent>
                <CardActions sx={{padding: '0 1rem 1.5rem 1rem'}}>
                    <Button variant={"contained"}
                            color={"primary"}
                            type="submit"
                    >Erstellen</Button>

                    <Button
                        variant={"outlined"}
                        onClick={_=>navigate("/account")}
                    >Verwerfen</Button>
                </CardActions>
            </form>

            {
                (!!newOfferData)?
                    <ConfirmationNewOfferDialog
                        id="confirmationNewOfferDialog"
                        keepMounted
                        open={open}
                        onClose={handleClose}
                        data={newOfferData!}
                    />
                    :<></>
            }


        </Card>
    )
}
