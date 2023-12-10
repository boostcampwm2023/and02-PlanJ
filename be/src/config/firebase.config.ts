import * as firebaseJson from "./firebase.config.json";

const firebaseConfig: Object = {
  type: firebaseJson.type,
  projectId: firebaseJson.project_id,
  privateKeyId: firebaseJson.private_key_id,
  privateKey: firebaseJson.private_key,
  clientEmail: firebaseJson.client_email,
  clientId: firebaseJson.client_id,
  authUri: firebaseJson.auth_uri,
  tokenUri: firebaseJson.token_uri,
  authProviderX509CertUrl: firebaseJson.auth_provider_x509_cert_url,
  clientX509CertUrl: firebaseJson.client_x509_cert_url,
  universeDomain: firebaseJson.universe_domain,
};

export default firebaseConfig;
