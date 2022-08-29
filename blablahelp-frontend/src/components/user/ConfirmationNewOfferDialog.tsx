import {Button, Dialog, DialogActions, DialogContent, DialogTitle, Typography} from "@mui/material";
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
    console.log("data received " + JSON.stringify(data))


    return(
        <Dialog
            maxWidth="md"
            open={open}

        >
            <DialogTitle>Zusammenfassung</DialogTitle>
            <DialogContent dividers>
                <Typography component={"p"}>
                    Shopping am {dateFromInstant(data.date!, currentLang+"-"+currentCountry)}<br/>
                    Lieferung an die Haustür zwischen {timeFromInstant(data.timeFrom!,currentLang+"-"+currentCountry)} und {timeFromInstant(data.timeTo!,currentLang+"-"+currentCountry)} Uhr
                </Typography>
                {
                    (parseInt(data.priceOffer!)>0)?
                        <Typography component={"p"}>
                            Angebot {data.priceOffer} Euro<br/>
                            Barzahlung für Assistance und gekaufte Produkte aus der Einkaufsliste bei Lieferung
                        </Typography>
                        :
                        <Typography component={"p"}>
                            Angebot ist kostenlos<br/>
                            Barzahlung für gekaufte Produkte aus der Einkaufsliste bei Lieferung
                        </Typography>
                }

                <Typography component={"p"}>
                    {data.shopname} – Zieladdresse<br/>
                    {addressToString(data.shopAddress!)}<br/>
                    {addressToString(data.destinationAddress!)}<br/>
                    Maximale Distance Breite {data.maxDistanceKm} km
                </Typography>

                <Typography component={"p"}>
                    Der Einkauf besteht aus Lebensmitteln und Getränken auf der vom Mitkäufer gelieferten Einkaufsliste<br/>
                    Maximale Anzahl an Mitshoppern {data.maxMitshoppers}<br/>
                    Maximale Anzahl für Produktartikel (ohne Flüssigkeiten) {data.maxArticles}<br/>
                    Höchstzahl für Flüssigkeiten {data.maxDrinks!} Liter
                </Typography>

                <Typography component={"p"}>
                    Anmerkungen<br/>
                    {(!!data.notes && data.notes.length > 0)?data.notes:"Keine"}
                </Typography>

                <Typography component={"p"}>
                    Sie können Ihr Angebot jederzeit stornieren,
                    wenn Sie noch keine Anfragen von Mitshoppern angenommen haben.<br/>
                    Wenn einige Mitkäufe bereits bestätigt sind,
                    ist eine Stornierung bis zum
                    {dateFromInstantMinusDays(data.timeFrom!,currentLang+"-"+currentCountry, cancellationDays)}
                    Uhr möglich (der Kündigungszähler in Ihrem Profil erhöht sich).<br/>
                    Danach ist eine Stornierung nur noch im Notfall möglich und bedarf der Rücksprache mit anderen.
                </Typography>

            </DialogContent>
            <DialogActions>
                <Button autoFocus onClick={_=>onClose(false)}>
                    Schließen
                </Button>
                <Button onClick={_=>onClose(true)}>Zustimmen und absenden</Button>
            </DialogActions>
        </Dialog>
    )
}
