# INF3405 Réseaux informatiques
Application Client-Serveur développées par moi-même et Laurent Pepin

## Server (Laurent)
CLI
Contient une BD
Contient un algo "Sobel"

recoit des requete avec user + psd
valide avec la bd, 
si existe pax:
  crée
sinon vérifie password

execute algo

envoie résultat


## Client (Adam)
CLI
Charge une image et envoie au serveur

UC1 -
envoie user + pwd
recoit réponse (MSG)
Si OK:
  envoie image
Attend une réponse
Affiche l'image

## Common
  - Classe MSG:
    - SUCCESS (0)
    - FAILURE (1)
