package com.toybrokers.ludo

import com.toybrokers.ludo.network.manageOngoingOnlineGamesService.ManageOngoingOnlineGamesService
import com.toybrokers.ludo.network.manageOpenOnlineGamesService.ManageOpenOnlineGamesService
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import kotlinx.rpc.serialization.json
import kotlinx.rpc.transport.ktor.server.RPC
import kotlinx.rpc.transport.ktor.server.rpc

const val SERVER_PORT = 8081
const val SERVER_HOST = "localhost"

val manageOngoingOnlineGamesRepository = ManageOngoingOnlineGamesDefaultRepository()

val manageOnlineGamesRepository = ManageOpenOnlineGamesDefaultRepository(
    manageOngoingOnlineGamesRepository
)

fun main() {
    embeddedServer(Netty, host = SERVER_HOST, port = SERVER_PORT) {
        install(RPC)
        routing {
            rpc("/ManageOpenOnlineGamesService") {
                rpcConfig {
                    serialization {
                        json {
                            allowStructuredMapKeys = true
                        }
                    }
                }

                registerService<ManageOpenOnlineGamesService> { coroutineContext ->
                    ManageOpenOnlineGamesDefaultService(
                        coroutineContext = coroutineContext,
                        manageOpenOnlineGamesRepository = manageOnlineGamesRepository
                    )
                }
            }
        }
        routing {
            rpc("/ManageOngoingOnlineGamesService") {
                rpcConfig {
                    serialization {
                        json {
                            allowStructuredMapKeys = true
                        }
                    }
                }

                registerService<ManageOngoingOnlineGamesService> { coroutineContext ->
                    ManageOngoingOnlineGamesDefaultService(
                        coroutineContext = coroutineContext,
                        manageOngoingOnlineGamesRepository = manageOngoingOnlineGamesRepository
                    )
                }
            }
        }
    }.start(wait = true)
}
