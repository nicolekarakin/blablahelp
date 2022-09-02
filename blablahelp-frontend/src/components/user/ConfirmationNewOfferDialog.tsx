import {Box, Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography} from "@mui/material";
import OwnOfferType from "../../types/OwnOfferType";
import {useContext} from "react";
import {AuthContext} from "../../shared/AuthProvider";
import {
    addressToString,
    cancellationDays,
    dateFromInstant,
    dateFromInstantMinusDays,
    timeFromInstant
} from "../../shared/util";

type ConfirmationNewOfferDialogProps={
    id: string;
    keepMounted: boolean;
    open: boolean;
    onClose: (value: boolean) => void;
    data:OwnOfferType;
}

export default function ConfirmationNewOfferDialog(props:ConfirmationNewOfferDialogProps){
    const { onClose, open, data } = props;
    const {currentLang, currentCountry} = useContext(AuthContext);

    return(
        <Dialog
            maxWidth="md"
            open={open}

        >
            <DialogTitle>Zusammenfassung</DialogTitle>
            <DialogContent dividers>
                <Typography sx={{fontSize: ".9rem"}} color="text.secondary" gutterBottom>
                    Lebensmitteleinkauf ist geplannt!
                </Typography>
                <Typography variant="h6" color={"primary"}>
                    {data.shopAddress?.city}, {dateFromInstant(data.shoppingDay!, currentLang+"-"+currentCountry)}
                </Typography>
                <Typography sx={{mb: 1.5}} color="text.secondary">
                    Lieferung an die Haustür zwischen <br/>
                    {timeFromInstant(data.timeFrom!,currentLang+"-"+currentCountry)} und {timeFromInstant(data.timeTo!,currentLang+"-"+currentCountry)} Uhr
                </Typography>

                {
                    (parseInt(data.priceOffer!)>0)?
                        <>
                        <Typography variant="h6" color={"primary"}>Angebot – {data.priceOffer} Euro</Typography>
                        <Typography  color="text.secondary">
                            Barzahlung für Assistance und gekaufte Produkte aus der Einkaufsliste bei Lieferung
                        </Typography>
                        </>
                        :
                        <>
                            <Typography variant="h6" color={"primary"}>Angebot ist kostenlos</Typography>
                            <Typography  color="text.secondary">
                                Barzahlung für gekaufte Produkte aus der Einkaufsliste bei Lieferung
                            </Typography>
                        </>
                }
                <Typography sx={{fontSize: "1rem", fontWeight:"bold",mb: 1.5}} color="text.primary">
                    Mitshoppern, maximal {data.maxMitshoppers}
                </Typography>

                <Typography variant="h6" color={"primary"}>Zwischen Adressen</Typography>
                <Typography   color="text.secondary">
                    {data.shopname} – {addressToString(data.shopAddress!)}<br/>
                    Zieladdresse – {addressToString(data.destinationAddress!)}<br/>
                </Typography>
                <Typography sx={{fontSize: "1rem", fontWeight:"bold",mb: 1.5}} color="text.primary">
                    Maximale Distance Breite – {data.maxDistanceKm} km
                </Typography>

                <Typography variant="h6" color={"primary"}>Einkaufsinhalt</Typography>
                <Typography   color="text.secondary">
                    Der Einkauf besteht aus Lebensmitteln und Getränken auf der vom Mitkäufer gelieferten Einkaufsliste
                </Typography>

                <Typography sx={{fontSize: "1rem", fontWeight:"bold",mb: 1.5}} color="text.primary">
                    Produktartikel**, maximal {data.maxArticles}<br/>
                    Flüssigkeiten, maximal {data.maxDrinks!} Liter<br/>
                    <Box component={"span"} sx={{fontSize:".8rem"}}>**ohne Flüssigkeiten</Box>
                </Typography>

                <Typography variant="h6" color={"primary"}>Anmerkungen</Typography>
                <Typography color="text.secondary" sx={{mb: 1.5}}>
                    {(!!data.notes && data.notes.length > 0)?data.notes:"Keine"}
                </Typography>

                <Typography variant="h6" color={"secondary"}>Stornierung</Typography>
                <Typography color="text.secondary" sx={{mb: 1.5}} variant={"body2"}>
                    Sie können Ihr Angebot jederzeit stornieren,
                    wenn Sie noch keine Anfragen von Mitshoppern angenommen haben.
                </Typography>
                <Typography color="text.secondary" sx={{mb: 1.5}} variant={"body2"}>
                    Wenn einige Mitkäufe bereits bestätigt sind,
                    ist eine Stornierung bis zum
                    {dateFromInstantMinusDays(data.timeFrom!,currentLang+"-"+currentCountry, cancellationDays)} Uhr möglich (der Kündigungszähler in Ihrem Profil erhöht sich).
                </Typography>
                <Typography color="text.secondary" sx={{mb: 1.5}} variant={"body2"}>
                    Danach ist eine Stornierung nur noch im Notfall möglich und bedarf der Rücksprache mit anderen.
                </Typography>

            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={_=>onClose(false)} variant={"outlined"}>
                    Schließen
                </Button>
                <Button onClick={_=>onClose(true)} variant={"contained"}>Zustimmen und absenden</Button>
            </DialogActions>
        </Dialog>
    )
}
