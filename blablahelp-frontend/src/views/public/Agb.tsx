import {Box, Stack, Typography} from "@mui/material"

const Agb = () => {

    return (
        <Stack direction="column"
               justifyContent="center"
               spacing={2}
               maxWidth={'md'}
        >
            <Box ml={2} mr={2} mt={3}>
                <Typography variant={'h1'}>AGBs – Nutzungsbedingungen</Typography>
                <Typography mb={'.4rem'} variant={'h4'}>§ 1 Allgemeine Regelungen</Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    1.1. BlaBlaHelp, Adresse? (nachfolgend „Anbieter“) stellt auf www.blablahelp.de eine Plattform zur
                    Verfügung über die registrierte Teilnehmer Kontakte für Hilfstätigkeiten suchen, anbieten und
                    organisieren können.
                    Über App ist es den Teilnehmern möglich miteinander in Kontakt zu treten und zu kommunizieren.
                    Die Nutzung des Apps ist Verbrauchern vorbehalten.
                    Verbraucher sind natürliche Personen, die ein Rechtsgeschäft zu Zwecken abschließen, die überwiegend
                    weder ihrer gewerblichen noch ihrer selbständigen beruflichen Tätigkeit zugerechnet werden können.
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    1.2. Diese Nutzungsbedingungen regeln die Bereitstellung des Portals durch den Anbieter und die
                    Nutzung des Portals durch ordnungsgemäß angemeldete Teilnehmer.
                </Typography>

                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 2 Registrierung</Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    2.1. Eine Nutzung des Aps ist nur nach vorheriger Registrierung möglich.
                    Ein Anspruch auf die Nutzung des Aps besteht nicht.
                    Der Anbieter ist berechtigt, Registrierungsanträge ohne Angabe von Gründen zurückzuweisen.
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    <b>2.2.</b> Die Nutzung des Portals ist kostenlos.
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    <b>2.3.</b> Eine Registrierung ist nur volljährigen und unbeschränkt geschäftsfähigen Personen
                    gestattet.
                    ??? Ab 16??Eine Registrierung minderjähriger Personen ist untersagt.
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    <b>2.5.</b> Der Teilnehmer verpflichtet sich, die vom Anbieter erfragten Kontaktdaten und sonstigen
                    Angaben korrekt und vollständig anzugeben.
                    Der Anbieter führt im Regelfall keine Überprüfung der Identität der Teilnehmer und im Portal
                    erfolgten Angaben durch.
                    Der Anbieter übernimmt daher keine Gewähr dafür, dass es sich bei dem im Inhaber eines Profils
                    tatsächlich um die Person handelt, für die der jeweilige Teilnehmer sich ausgibt.
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    <b>2.6.</b> Nach Angabe aller erfragten Daten durch den Teilnehmer werden diese vom Anbieter auf
                    Vollständigkeit und Plausibilität überprüft.
                    Sofern die Angaben aus Sicht des Anbieters korrekt sind und keine sonstigen Bedenken bestehen,
                    schaltet der Anbieter den Zugang frei und benachrichtigt den Teilnehmer hiervon per E-Mail.
                    Diese E-Mail gilt als Annahme des Registrierungsantrags.
                    Ab Zugang der E-Mail ist der Teilnehmer zum Zugang und zur Nutzung des Portals im Rahmen der
                    Nutzungsbedingungen berechtigt.
                </Typography>
                <Typography component={'p'} mb={'1.2rem'}>
                    <b>2.7.</b> Der Teilnehmer ist selbst dafür verantwortlich, dass die in seinem Verantwortungsbereich
                    liegenden technischen Voraussetzungen für eine Nutzung vorliegen.
                    Der Anbieter schuldet diesbezüglich keine Beratung.
                </Typography>

                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 3 Verantwortung für Zugangsdaten, Aktualität der
                    Zugangsdaten</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 4 Beendigung der Teilnahme</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 5 Angebot des Aps</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 6 Verfügbarkeit</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 7 Änderung des Angebotes</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 8 Schutz von Inhalten</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 9 Einstellen von Inhalten durch
                    Teilnehmer</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 10 Nutzungsrecht der Teilnehmer an den
                    Inhalten</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 11 Verbotene Aktivitäten</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 12 Sperrung von Zugängen</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 13 Haftung</Typography>
                <Typography mb={'.4rem'} mt={'1rem'} variant={'h4'}>§ 14 Sonstiges</Typography>

                <Typography component={'p'}>
                    Stand: August 2022
                </Typography>


            </Box>

        </Stack>
    )
}
export default Agb