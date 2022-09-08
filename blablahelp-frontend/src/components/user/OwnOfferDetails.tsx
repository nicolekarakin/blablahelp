import {
    Box,
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle,
    Typography,
    useMediaQuery,
    useTheme
} from "@mui/material";
import OwnOfferType from "../../types/OwnOfferType";
import {useContext, useState} from "react";
import {AuthContext} from "../../shared/AuthProvider";
import {
    addressToString,
    cancellationDays,
    dateFromInstant,
    dateFromInstantMinusDays,
    timeFromInstant
} from "../../shared/util";

type OwnOfferDetailsProps={
    data:OwnOfferType;
}

export default function OwnOfferDetails(props:OwnOfferDetailsProps){

    const {currentLang, currentCountry} = useContext(AuthContext);
    const [open, setOpen] = useState(false);
    const theme = useTheme();
    const fullScreen = useMediaQuery(theme.breakpoints.down('md'));

    const handleClickOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };

    return (
        <>
            <Button onClick={handleClickOpen} color={"primary"} variant={"text"} size={"small"} sx={{display:"inline-block"}}>Ansehen</Button>
            <Dialog
                fullScreen={fullScreen}
                open={open}
                onClose={handleClose}
                aria-labelledby="details-ansehen"
            >
                <DialogTitle id="details-ansehen">
                    {props.data.shopAddress?.city}, {dateFromInstant(props.data.shoppingDay!, currentLang+"-"+currentCountry)}
                </DialogTitle>
                <DialogContent>
                    <DialogContentText component={"div"}>
                        <Typography sx={{mb: 1.5}} color="text.secondary">
                            Lieferung an die Haustür zwischen <br/>
                            {timeFromInstant(props.data.timeFrom!, currentLang + "-" + currentCountry)} und {timeFromInstant(props.data.timeTo!, currentLang + "-" + currentCountry)} Uhr
                        </Typography>
                        <Typography variant="h6" color={"primary"}>Angebot – {props.data.priceOffer} Euro</Typography>
                        <Typography color="text.secondary">
                            Barzahlung für Assistance und gekaufte Produkte aus der Einkaufsliste bei Lieferung
                        </Typography>
                        <Typography sx={{fontSize: "1rem", fontWeight: "bold", mb: 1.5}} color="text.primary">
                            Mitshoppern, maximal {props.data.maxMitshoppers}
                        </Typography>

                        <Typography variant="h6" color={"primary"}>Zwischen Adressen</Typography>
                        <Typography   color="text.secondary">
                            {props.data.shopname} – {addressToString(props.data.shopAddress!)}<br/>
                            Zieladdresse – {addressToString(props.data.destinationAddress!)}<br/>
                        </Typography>
                        <Typography sx={{fontSize: "1rem", fontWeight:"bold",mb: 1.5}} color="text.primary">
                            Maximale Distance Breite – {props.data.maxDistanceKm} km
                        </Typography>


                        <Typography variant="h6" color={"primary"}>Einkaufsinhalt</Typography>
                        <Typography   color="text.secondary">
                            Der Einkauf besteht aus Lebensmitteln und Getränken auf der vom Mitkäufer gelieferten Einkaufsliste
                        </Typography>

                        <Typography sx={{fontSize: "1rem", fontWeight:"bold",mb: 1.5}} color="text.primary">
                            Produktartikel**, maximal {props.data.maxArticles}<br/>
                            Flüssigkeiten, maximal {props.data.maxLiter!} Liter<br/>
                            <Box component={"span"} sx={{fontSize:".8rem"}}>**ohne Flüssigkeiten</Box>
                        </Typography>

                        <Typography variant="h6" color={"primary"}>Anmerkungen</Typography>
                        <Typography color="text.secondary" sx={{mb: 1.5}}>
                            {(!!props.data.notes && props.data.notes.length > 0)?props.data.notes:"Keine"}
                        </Typography>

                        <Typography variant="h6" color={"secondary"}>Stornierung</Typography>
                        <Typography color="text.secondary" sx={{mb: 1.5}} variant={"body2"}>
                            Sie können Ihr Angebot jederzeit stornieren,
                            wenn Sie noch keine Anfragen von Mitshoppern angenommen haben.
                        </Typography>
                        <Typography color="text.secondary" sx={{mb: 1.5}} variant={"body2"}>
                            Wenn einige Mitkäufe bereits bestätigt sind,
                            ist eine Stornierung bis zum
                            {dateFromInstantMinusDays(props.data.timeFrom!,currentLang+"-"+currentCountry, cancellationDays)} Uhr möglich (der Kündigungszähler in Ihrem Profil erhöht sich).
                        </Typography>
                        <Typography color="text.secondary" sx={{mb: 1.5}} variant={"body2"}>
                            Danach ist eine Stornierung nur noch im Notfall möglich und bedarf der Rücksprache mit anderen.
                        </Typography>
                    </DialogContentText>
                </DialogContent>
                <DialogActions>
                    <Button autoFocus onClick={handleClose}>Schließen</Button>

                </DialogActions>
            </Dialog>
        </>
    )

}
