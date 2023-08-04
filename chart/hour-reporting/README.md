# Helm chart for the Hour reporting


### Configuration

The following tables lists the configurable parameters

| Parameter                                      | Default                                                                    | Description
| ---------------------------------------------- | ---------------------------------------------------------------------------| ---
| `options.java`                                 |                                                                            | Java options that should be passed to the application jar
| `options.application`                          |                                                                            | Options that should be passed to the application
| `configuration.database.core.core.host`        | `3306`                                                                     | Database connection port
| `configuration.database.core.port`             |                                                                            | Database connection host
| `configuration.database.core.username`         |                                                                            | Database connection username
| `configuration.database.core.password`         |                                                                            | Database connection password
| `configuration.database.core.schema`           |                                                                            | Database default schema
| `secret.database.core.existing`                | `false`                                                                    | Whether to use an existing JWT secret or have the chart create a new one
| `secret.database.core.name`                    | `openid-core-secret`                                                       | Whether to use an existing JWT secret or have the chart create a new one
| `secret.database.core.usernameKey`             | `database.username`                                                        | Key in database secret to use to which contains the connection username
| `secret.database.core.passwordKey`             | `database.password`                                                        | Key in database secret to use to which contains the connection password
| `configuration.database.core.core.host`        | `3306`                                                                     | Database connection port
| `configuration.database.swyxsecurity.port`     |                                                                            | Database connection host
| `configuration.database.swyxsecurity.username` |                                                                            | Database connection username
| `configuration.database.swyxsecurity.password` |                                                                            | Database connection password
| `configuration.database.swyxsecurity.schema`   |                                                                            | Database default schema
| `secret.database.swyxsecurity.existing`        | `false`                                                                    | Whether to use an existing JWT secret or have the chart create a new one
| `secret.database.swyxsecurity.name`            | `openid-swyxsecurity-secret`                                                       | Whether to use an existing JWT secret or have the chart create a new one
| `secret.database.swyxsecurity.usernameKey`     | `database.username`                                                        | Key in database secret to use to which contains the connection username
| `secret.database.swyxsecurity.passwordKey`     | `database.password`                                                        | Key in database secret to use to which contains the connection password
| `replicaCount`                                 | `1`                                                                        |
| `resources.requests.memory`                    | `128Mi`                                                                    |
| `resources.requests.cpu`                       | `500m`                                                                     |
| `resources.limits.memory`                      | `256Mi`                                                                    |
| `resources.limits.cpu`                         | `750m`                                                                     |
| `nodeSelector`                                 | `{}`                                                                       |
| `tolerations`                                  | `[]`                                                                       |
| `affinity`                                     | `{}`                                                                       |
| `volumeMounts`                                 | `[]`                                                                       |
| `volumes`                                      | `[]`                                                                       |
| `envFrom`                                      | `[]`                                                                       |
| `env`                                          | `[]`                                                                       |
| `initContainers`                               | `[]`                                                                       |
| `ingress.enabled`                              | `false`                                                                    |
| `ingress.annotations`                          | `{}`                                                                       |
| `ingress.path`                                 | `/`                                                                        |
| `ingress.hosts`                                | `[]`                                                                       |
| `ingress.tls`                                  | `[]`                                                                       |
| `service.type`                                 | `ClusterIP`                                                                |
| `service.port`                                 | `8080`                                                                     |
| `image.repository`                             | `registry.k8s.ispworks.nl/operator-ks`                                     |
| `image.image`                                  | `summa/openid`                                                        |
| `image.tag`                                    | `snapshot`                                                                 |
| `image.pullPolicy`                             | `Always`                                                                   |
| `image.pullSecret`                             |                                                                            |
| `nameOverride`                                 |                                                                            |
| `fullnameOverride`                             |                                                                            |

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`.
