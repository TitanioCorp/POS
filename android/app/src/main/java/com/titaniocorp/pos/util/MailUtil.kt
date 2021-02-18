package com.titaniocorp.pos.util

import java.util.*
import com.titaniocorp.pos.BuildConfig
import com.titaniocorp.pos.app.model.Billing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

enum class TypeEmail{
    BILLING,
    REPORT
}

object MailUtil {
    private fun getProperties(): Properties = Properties().apply {
        put("mail.smtp.host", "smtp.gmail.com")
        put("mail.smtp.socketFactory.port", "465")
        put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
        put("mail.smtp.auth", "true")
        put("mail.smtp.port", "465")
    }

    private fun getSession(properties: Properties) = Session.getDefaultInstance(properties, object : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(BuildConfig.EMAIL_USERNAME, BuildConfig.EMAIL_PASSWORD)
        }
    })

    fun sendBilling(
        billing: Billing,
        billingFile: File?,
        purchasesFile: File?,
        paymentsPurchasesFile: File?,
        stocksFile: File?,
        paymentsFile: File?): String?{

        return try {
            val date = Date()

            MimeMessage(getSession(getProperties())).apply {
                addRecipient(Message.RecipientType.TO, InternetAddress(Configurations.emailAdmin))
                subject = "Cierre de caja - ${Date().toFormatString()}"
                sentDate = date
                setFrom(InternetAddress(BuildConfig.EMAIL_USERNAME, "Titanio Corporate"))

                setContent(MimeMultipart().apply {

                    addBodyPart(MimeBodyPart().apply {
                        setContent(HtmlUtil.getBilling(billing), "text/html")
                    })

                    billingFile?.let {
                        addBodyPart(MimeBodyPart().apply {
                            attachFile(it)
                            fileName = "billing-${date.toFormatFileString()}.csv"
                        })
                    }

                    purchasesFile?.let {
                        addBodyPart(MimeBodyPart().apply {
                            attachFile(it)
                            fileName = "purchases-${date.toFormatFileString()}.csv"
                        })
                    }

                    paymentsPurchasesFile?.let {
                        addBodyPart(MimeBodyPart().apply {
                            attachFile(it)
                            fileName = "payments-purchases-${date.toFormatFileString()}.csv"
                        })
                    }

                    stocksFile?.let {
                        addBodyPart(MimeBodyPart().apply {
                            attachFile(it)
                            fileName = "stocks-${date.toFormatFileString()}.csv"
                        })
                    }

                    paymentsFile?.let {
                        addBodyPart(MimeBodyPart().apply {
                            attachFile(it)
                            fileName = "payments-${date.toFormatFileString()}.csv"
                        })
                    }
                })
            }.also {
                Transport.send(it)
                Timber.tag(Constants.TAG_APP_DEBUG).d("Email sent successfully")
            }
            null
        } catch (exception: MessagingException) {
            Timber.tag(Constants.TAG_APP_DEBUG).e(exception)
            "MessagingException: ${exception.message}"
        } catch (exception: SendFailedException){
            "SendFailedException: ${exception.message}"
        }
    }

    fun sendReport(
        startDate: Date,
        endDate: Date,
        billing: Billing,
        billingFile: File?,
        purchasesFile: File?,
        paymentsPurchasesFile: File?,
        stocksFile: File?,
        paymentsFile: File?){

        try {
            runBlocking(Dispatchers.Default){
                val date = Date()

                MimeMessage(getSession(getProperties())).apply {
                    addRecipient(Message.RecipientType.TO, InternetAddress("juanortizbayona@gmail.com"))
                    subject = "Reporte - ${startDate.toFormatString()} a ${endDate.toFormatString()}"
                    sentDate = date
                    setFrom(InternetAddress("titaniocorporate@gmail.com", "Titanio Corporate"))

                    setContent(MimeMultipart().apply {

                        addBodyPart(MimeBodyPart().apply {
                            setContent(HtmlUtil.getReport(startDate, endDate, billing), "text/html")
                        })

                        billingFile?.let {
                            addBodyPart(MimeBodyPart().apply {
                                attachFile(it)
                                fileName = "billing-${date.toFormatFileString()}.csv"
                            })
                        }

                        purchasesFile?.let {
                            addBodyPart(MimeBodyPart().apply {
                                attachFile(it)
                                fileName = "purchases-${date.toFormatFileString()}.csv"
                            })
                        }

                        paymentsPurchasesFile?.let {
                            addBodyPart(MimeBodyPart().apply {
                                attachFile(it)
                                fileName = "payments-purchases-${date.toFormatFileString()}.csv"
                            })
                        }

                        stocksFile?.let {
                            addBodyPart(MimeBodyPart().apply {
                                attachFile(it)
                                fileName = "stocks-${date.toFormatFileString()}.csv"
                            })
                        }

                        paymentsFile?.let {
                            addBodyPart(MimeBodyPart().apply {
                                attachFile(it)
                                fileName = "payments-${date.toFormatFileString()}.csv"
                            })
                        }
                    })
                }.also {
                    Transport.send(it)
                    Timber.tag(Constants.TAG_APP_DEBUG).d("Email sent successfully")
                }
            }
        } catch (exception: MessagingException) {
            Timber.tag(Constants.TAG_APP_DEBUG).e(exception)
        }
    }
}