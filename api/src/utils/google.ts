import { GoogleTokenResponse } from "../types/google-token-response";

export const getGoogleTokenData = async (
  token: string
): Promise<GoogleTokenResponse> => {
  const url = `https://oauth2.googleapis.com/tokeninfo?id_token=${token}`;
  const res = await fetch(url);
  if (res.ok) return (await res.json()) as GoogleTokenResponse;
  return null;
};
