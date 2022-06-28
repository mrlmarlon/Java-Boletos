# Java-Boletos

[![MIT License](https://img.shields.io/github/license/Java-Brasil/Java-Boletos.svg) ](https://github.com/Java-Brasil/Java-Boletos/blob/master/LICENSE) [![Maven Central](https://img.shields.io/maven-central/v/br.com.swconsultoria/java-boletos.svg?label=Maven%20Central)](https://search.maven.org/artifact/br.com.swconsultoria/java-boletos/1.0.1/jar) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/Java-Brasil/Java-Boletos.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Java-Brasil/Java-Boletos/context:java) [![Total alerts](https://img.shields.io/lgtm/alerts/g/Java-Brasil/Java-Boletos.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/Java-Brasil/Java-Boletos/alerts/)

Projeto Open Source para geração de boletos dos bancos criado e mantido pela comunidade JavaBrasil

## Dúvidas, Sugestões ou Consultorias

Entre no Discord do Projeto: https://discord.gg/ZXpqnaV

Para Iniciar :

- Caso use Libs baixe o java-boletos.jar (https://github.com/Java-Brasil/Java-Boletos/raw/master/java-boletos.jar) e o
  adicione às bibliotecas de seu projeto.

- Maven :

```xml
<dependency>
    <groupId>br.com.swconsultoria</groupId>
    <artifactId>java-boletos</artifactId>
    <version>1.0.1</version>
</dependency>
```

- Gradle :

```groovy
repositories {
    maven {
        url = "https://oss.sonatype.org/content/repositories/snapshots"
    }
}
dependencies {
    implementation "br.com.swconsultoria:java-boletos:1.0.1"
}
```

Veja a Wiki https://github.com/Java-Brasil/Java-Boletos/wiki, para ter um Tutorial Completo.

________________________________________________________________________________________________

## Boletos/Bancos Desenvolvidos
| **Banco / Entidade** | **Tipo** |**Envio**|**Alteração**|**Consulta**|**Baixa**|**Impressão**|
|----------------------|----------|-----|---------------|-----|-----|-----|
| Bradesco             | API      |✅| ❌|❌|❌|🕐|
| Itaú                 | API      |✅| ❌|❌|❌|🕐|
| Sicoob               | API      |✅| 🕐|✅|🕐|✅|
| Safe2Pay             | API      |✅| ❌|❌|❌|✅|
| BanriSul             | API      |✅| ✅|✅|✅|✅|
| Sicoob               | CNAB 240 |✅| ✅|✅|✅|✅|
| Sicred               | CNAB 400 |✅| ✅|✅|✅|✅|

- ✅ : Pronto
- 🕐 : Em Desenvolvimento
- ❌ : Banco não Disponibiliza Função 

________________________________________________________________________________________________

# Historico de Versões

## v1.0.1 - 28/06/2022
- Adicionado Itaú
- Adicionado Safe2Pay
- Correção de layout de Sicoob CNAB 240

## v1.0.0 - 11/06/2022 
- Versão Inicial
- Adicionado Bradesco API
- Adicionado Sicoob API
- Adicionado Banrisul API
- Adicionado Sicoob CNAB 240
- Adicionado Sicred CNAB 400
