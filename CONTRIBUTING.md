# Contributing

## Testing

```
$ nix-shell
$ sbt test
```

## Publishing

war-runner uses the process outlined in the [Using
Sonatype](https://www.scala-sbt.org/release/docs/Using-Sonatype.html)
section of the sbt manual for publishing to Maven Central via Sonatype.

Create a staging release in Sonatype:

```
$ nix-shell
$ sbt
> set version := "10.1.28.0.0-M2"
> publishSigned
```

Review it:

* Go to [Staging
  Repositories](https://oss.sonatype.org/#stagingRepositories) on Nexus
  Repository Manager
* Review the contents of the staging repository


Release it:

* Close the staging repository
* Release the staging repository to promote to Maven Central

Wait for it to be synced to Maven Central:

* <https://repo1.maven.org/maven2/com/earldouglas/war-runner/>

Update the documentation:

```
$ git checkout -b v10.1.28.0.0-M2
$ sed -i 's/10\.1\.28\.0\.0-M1/10.1.28.0.0-M2/g' README.md
$ git add README.md
$ git commit -m "Update version to 10.1.28.0.0-M2"
$ git push origin v10.1.28.0.0-M2
```

Tag the release:

```
$ git tag 10.1.28.0.0-M2
$ git push --tags origin
```
