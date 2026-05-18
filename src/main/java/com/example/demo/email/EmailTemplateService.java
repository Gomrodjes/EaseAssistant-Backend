package com.example.demo.email;

import org.springframework.stereotype.Service;

@Service
public class EmailTemplateService {

    public String buildVerificationEmail(String fullName, String verificationUrl) {
        String displayName = (fullName == null || fullName.isBlank()) ? "Hola" : "Hola " + fullName;

        return """
                <!DOCTYPE html>
                <html lang="es">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Verificacion de cuenta</title>
                </head>
                <body style="margin:0; padding:24px; background-color:#f4efe8; font-family:Arial, Helvetica, sans-serif; color:#1f2937;">
                    <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="border-collapse:collapse;">
                        <tr>
                            <td align="center">
                                <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="max-width:640px; border-collapse:collapse; background-color:#ffffff; border-radius:24px; overflow:hidden; box-shadow:0 10px 30px rgba(15, 23, 42, 0.08);">
                                    <tr>
                                        <td style="padding:40px 40px 24px; background:linear-gradient(135deg, #1d4ed8, #0f172a); color:#ffffff;">
                                            <p style="margin:0 0 12px; font-size:13px; letter-spacing:1.5px; text-transform:uppercase; opacity:0.85;">EaseAssistant</p>
                                            <h1 style="margin:0; font-size:30px; line-height:1.2;">Verifica tu cuenta</h1>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:36px 40px 12px;">
                                            <p style="margin:0 0 16px; font-size:18px; font-weight:bold; color:#111827;">%s,</p>
                                            <p style="margin:0 0 16px; font-size:16px; line-height:1.7; color:#4b5563;">
                                                Ya casi esta todo listo. Solo te falta confirmar tu correo para activar tu cuenta y continuar en EaseAssistant.
                                            </p>
                                            <p style="margin:0 0 28px; font-size:16px; line-height:1.7; color:#4b5563;">
                                                Pulsa el boton de abajo para completar la verificacion.
                                            </p>
                                            <table role="presentation" cellspacing="0" cellpadding="0" style="margin:0 0 28px;">
                                                <tr>
                                                    <td style="border-radius:999px; background-color:#1d4ed8;">
                                                        <a href="%s" style="display:inline-block; padding:14px 28px; font-size:16px; font-weight:bold; color:#ffffff; text-decoration:none;">
                                                            Verificar cuenta
                                                        </a>
                                                    </td>
                                                </tr>
                                            </table>
                                            <p style="margin:0 0 12px; font-size:14px; line-height:1.7; color:#6b7280;">
                                                Si el boton no funciona, copia y pega este enlace en tu navegador:
                                            </p>
                                            <p style="margin:0 0 28px; word-break:break-all;">
                                                <a href="%s" style="font-size:14px; color:#1d4ed8; text-decoration:none;">%s</a>
                                            </p>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="padding:0 40px 36px;">
                                            <div style="padding:16px 18px; border-radius:16px; background-color:#f9fafb; font-size:13px; line-height:1.7; color:#6b7280;">
                                                Si no has solicitado esta verificacion, puedes ignorar este mensaje con tranquilidad.
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </body>
                </html>
                """.formatted(displayName, verificationUrl, verificationUrl, verificationUrl);
    }
}
