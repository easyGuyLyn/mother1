.class Lcom/regus/base/control/SpalashActivity$1;
.super Ljava/lang/Object;
.source "SpalashActivity.java"

# interfaces
.implements Ljava/lang/Runnable;


# annotations
.annotation system Ldalvik/annotation/EnclosingMethod;
    value = Lcom/regus/base/control/SpalashActivity;->getData()V
.end annotation

.annotation system Ldalvik/annotation/InnerClass;
    accessFlags = 0x0
    name = null
.end annotation


# instance fields
.field final synthetic this$0:Lcom/regus/base/control/SpalashActivity;


# direct methods
.method constructor <init>(Lcom/regus/base/control/SpalashActivity;)V
    .registers 2
    .param p1, "this$0"    # Lcom/regus/base/control/SpalashActivity;

    .prologue
    .line 90
    iput-object p1, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    invoke-direct {p0}, Ljava/lang/Object;-><init>()V

    return-void
.end method


# virtual methods
.method public run()V
    .registers 16

    .prologue
    .line 94
    :try_start_0
    iget-object v12, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    # getter for: Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;
    invoke-static {v12}, Lcom/regus/base/control/SpalashActivity;->access$000(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;

    move-result-object v12

    invoke-static {v12}, Landroid/text/TextUtils;->isEmpty(Ljava/lang/CharSequence;)Z

    move-result v12

    if-eqz v12, :cond_19

    .line 95
    iget-object v12, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    invoke-static {}, Lcom/regus/base/HostManager;->getInstance()Lcom/regus/base/HostManager;

    move-result-object v13

    invoke-virtual {v13}, Lcom/regus/base/HostManager;->getAim_url()Ljava/lang/String;

    move-result-object v13

    # setter for: Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;
    invoke-static {v12, v13}, Lcom/regus/base/control/SpalashActivity;->access$002(Lcom/regus/base/control/SpalashActivity;Ljava/lang/String;)Ljava/lang/String;

    .line 97
    :cond_19
    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    const-string v13, "aid="

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    invoke-static {}, Lcom/regus/base/HostManager;->getInstance()Lcom/regus/base/HostManager;

    move-result-object v13

    invoke-virtual {v13}, Lcom/regus/base/HostManager;->getAppId()Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    const-string v13, "&sid="

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    invoke-static {}, Lcom/regus/base/HostManager;->getInstance()Lcom/regus/base/HostManager;

    move-result-object v13

    invoke-virtual {v13}, Lcom/regus/base/HostManager;->getmSid()Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v11

    .line 98
    .local v11, "urlDate":Ljava/lang/String;
    new-instance v9, Ljava/net/URL;

    new-instance v12, Ljava/lang/StringBuilder;

    invoke-direct {v12}, Ljava/lang/StringBuilder;-><init>()V

    iget-object v13, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    # getter for: Lcom/regus/base/control/SpalashActivity;->mPostDetailUrl:Ljava/lang/String;
    invoke-static {v13}, Lcom/regus/base/control/SpalashActivity;->access$100(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;

    move-result-object v13

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    const-string v13, "?"

    invoke-virtual {v12, v13}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    invoke-virtual {v12, v11}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v12

    invoke-virtual {v12}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v12

    invoke-direct {v9, v12}, Ljava/net/URL;-><init>(Ljava/lang/String;)V

    .line 99
    .local v9, "url":Ljava/net/URL;
    invoke-virtual {v9}, Ljava/net/URL;->openConnection()Ljava/net/URLConnection;

    move-result-object v10

    check-cast v10, Ljava/net/HttpURLConnection;

    .line 100
    .local v10, "urlConnection":Ljava/net/HttpURLConnection;
    const/16 v12, 0x1388

    invoke-virtual {v10, v12}, Ljava/net/HttpURLConnection;->setConnectTimeout(I)V

    .line 101
    const/16 v12, 0x1388

    invoke-virtual {v10, v12}, Ljava/net/HttpURLConnection;->setReadTimeout(I)V

    .line 102
    const-string v12, "GET"

    invoke-virtual {v10, v12}, Ljava/net/HttpURLConnection;->setRequestMethod(Ljava/lang/String;)V

    .line 103
    invoke-virtual {v10}, Ljava/net/HttpURLConnection;->connect()V

    .line 104
    invoke-virtual {v10}, Ljava/net/HttpURLConnection;->getResponseCode()I

    move-result v2

    .line 105
    .local v2, "code":I
    const-string v12, "jumpuUrl"

    new-instance v13, Ljava/lang/StringBuilder;

    invoke-direct {v13}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v10}, Ljava/net/HttpURLConnection;->getResponseCode()I

    move-result v14

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(I)Ljava/lang/StringBuilder;

    move-result-object v13

    const-string v14, "..."

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v13

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v13

    invoke-static {v12, v13}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 106
    const/16 v12, 0xc8

    if-ne v2, v12, :cond_182

    .line 107
    invoke-virtual {v10}, Ljava/net/HttpURLConnection;->getInputStream()Ljava/io/InputStream;

    move-result-object v5

    .line 108
    .local v5, "inputStream":Ljava/io/InputStream;
    new-instance v1, Ljava/io/BufferedReader;

    new-instance v12, Ljava/io/InputStreamReader;

    invoke-direct {v12, v5}, Ljava/io/InputStreamReader;-><init>(Ljava/io/InputStream;)V

    invoke-direct {v1, v12}, Ljava/io/BufferedReader;-><init>(Ljava/io/Reader;)V

    .line 110
    .local v1, "bufferedReader":Ljava/io/BufferedReader;
    new-instance v0, Ljava/lang/StringBuffer;

    invoke-direct {v0}, Ljava/lang/StringBuffer;-><init>()V

    .line 111
    .local v0, "buffer":Ljava/lang/StringBuffer;
    :goto_b7
    invoke-virtual {v1}, Ljava/io/BufferedReader;->readLine()Ljava/lang/String;

    move-result-object v7

    .local v7, "line":Ljava/lang/String;
    if-eqz v7, :cond_cf

    .line 112
    invoke-virtual {v0, v7}, Ljava/lang/StringBuffer;->append(Ljava/lang/String;)Ljava/lang/StringBuffer;
    :try_end_c0
    .catch Ljava/lang/Exception; {:try_start_0 .. :try_end_c0} :catch_c1

    goto :goto_b7

    .line 146
    .end local v0    # "buffer":Ljava/lang/StringBuffer;
    .end local v1    # "bufferedReader":Ljava/io/BufferedReader;
    .end local v2    # "code":I
    .end local v5    # "inputStream":Ljava/io/InputStream;
    .end local v7    # "line":Ljava/lang/String;
    .end local v9    # "url":Ljava/net/URL;
    .end local v10    # "urlConnection":Ljava/net/HttpURLConnection;
    .end local v11    # "urlDate":Ljava/lang/String;
    :catch_c1
    move-exception v4

    .line 147
    .local v4, "e":Ljava/lang/Exception;
    iget-object v12, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    const/4 v13, 0x1

    iget-object v14, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    # getter for: Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;
    invoke-static {v14}, Lcom/regus/base/control/SpalashActivity;->access$000(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;

    move-result-object v14

    # invokes: Lcom/regus/base/control/SpalashActivity;->jump(ZLjava/lang/String;)V
    invoke-static {v12, v13, v14}, Lcom/regus/base/control/SpalashActivity;->access$200(Lcom/regus/base/control/SpalashActivity;ZLjava/lang/String;)V

    .line 150
    .end local v4    # "e":Ljava/lang/Exception;
    :cond_ce
    :goto_ce
    return-void

    .line 114
    .restart local v0    # "buffer":Ljava/lang/StringBuffer;
    .restart local v1    # "bufferedReader":Ljava/io/BufferedReader;
    .restart local v2    # "code":I
    .restart local v5    # "inputStream":Ljava/io/InputStream;
    .restart local v7    # "line":Ljava/lang/String;
    .restart local v9    # "url":Ljava/net/URL;
    .restart local v10    # "urlConnection":Ljava/net/HttpURLConnection;
    .restart local v11    # "urlDate":Ljava/lang/String;
    :cond_cf
    :try_start_cf
    invoke-virtual {v0}, Ljava/lang/StringBuffer;->toString()Ljava/lang/String;

    move-result-object v6

    .line 115
    .local v6, "jsonStr":Ljava/lang/String;
    const-string v12, "regus"

    new-instance v13, Ljava/lang/StringBuilder;

    invoke-direct {v13}, Ljava/lang/StringBuilder;-><init>()V

    invoke-virtual {v13, v6}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v13

    const-string v14, ""

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v13

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v13

    invoke-static {v12, v13}, Landroid/util/Log;->e(Ljava/lang/String;Ljava/lang/String;)I
    :try_end_eb
    .catch Ljava/lang/Exception; {:try_start_cf .. :try_end_eb} :catch_c1

    .line 119
    :try_start_eb
    new-instance v8, Lorg/json/JSONObject;

    const-string v12, "\\"

    const-string v13, ""

    invoke-virtual {v6, v12, v13}, Ljava/lang/String;->replace(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;

    move-result-object v12

    invoke-direct {v8, v12}, Lorg/json/JSONObject;-><init>(Ljava/lang/String;)V

    .line 120
    .local v8, "responseJson":Lorg/json/JSONObject;
    const-string v12, "Status"

    invoke-virtual {v8, v12}, Lorg/json/JSONObject;->has(Ljava/lang/String;)Z

    move-result v12

    if-eqz v12, :cond_ce

    const-string v12, "Data"

    invoke-virtual {v8, v12}, Lorg/json/JSONObject;->has(Ljava/lang/String;)Z

    move-result v12

    if-eqz v12, :cond_ce

    .line 121
    const-string v12, "Status"

    invoke-virtual {v8, v12}, Lorg/json/JSONObject;->getBoolean(Ljava/lang/String;)Z

    move-result v12

    if-eqz v12, :cond_174

    .line 123
    new-instance v3, Lorg/json/JSONObject;

    const-string v12, "Data"

    invoke-virtual {v8, v12}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v12

    invoke-direct {v3, v12}, Lorg/json/JSONObject;-><init>(Ljava/lang/String;)V

    .line 126
    .local v3, "dataJsonObject":Lorg/json/JSONObject;
    const-string v12, "IsEnable"

    invoke-virtual {v3, v12}, Lorg/json/JSONObject;->getBoolean(Ljava/lang/String;)Z

    move-result v12

    if-eqz v12, :cond_166

    .line 128
    const-string v12, "jumpuUrl"

    new-instance v13, Ljava/lang/StringBuilder;

    invoke-direct {v13}, Ljava/lang/StringBuilder;-><init>()V

    const-string v14, "Url"

    invoke-virtual {v3, v14}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v14

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v13

    const-string v14, "\u8bf7\u6c42\u6210\u529f\uff01"

    invoke-virtual {v13, v14}, Ljava/lang/StringBuilder;->append(Ljava/lang/String;)Ljava/lang/StringBuilder;

    move-result-object v13

    invoke-virtual {v13}, Ljava/lang/StringBuilder;->toString()Ljava/lang/String;

    move-result-object v13

    invoke-static {v12, v13}, Landroid/util/Log;->d(Ljava/lang/String;Ljava/lang/String;)I

    .line 129
    iget-object v12, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    const-string v13, "IsEnable"

    invoke-virtual {v3, v13}, Lorg/json/JSONObject;->getBoolean(Ljava/lang/String;)Z

    move-result v13

    const-string v14, "Url"

    invoke-virtual {v3, v14}, Lorg/json/JSONObject;->getString(Ljava/lang/String;)Ljava/lang/String;

    move-result-object v14

    # invokes: Lcom/regus/base/control/SpalashActivity;->jump(ZLjava/lang/String;)V
    invoke-static {v12, v13, v14}, Lcom/regus/base/control/SpalashActivity;->access$200(Lcom/regus/base/control/SpalashActivity;ZLjava/lang/String;)V
    :try_end_152
    .catch Lorg/json/JSONException; {:try_start_eb .. :try_end_152} :catch_154
    .catch Ljava/lang/Exception; {:try_start_eb .. :try_end_152} :catch_c1

    goto/16 :goto_ce

    .line 139
    .end local v3    # "dataJsonObject":Lorg/json/JSONObject;
    .end local v8    # "responseJson":Lorg/json/JSONObject;
    :catch_154
    move-exception v4

    .line 140
    .local v4, "e":Lorg/json/JSONException;
    :try_start_155
    invoke-virtual {v4}, Lorg/json/JSONException;->printStackTrace()V

    .line 141
    iget-object v12, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    const/4 v13, 0x1

    iget-object v14, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    # getter for: Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;
    invoke-static {v14}, Lcom/regus/base/control/SpalashActivity;->access$000(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;

    move-result-object v14

    # invokes: Lcom/regus/base/control/SpalashActivity;->jump(ZLjava/lang/String;)V
    invoke-static {v12, v13, v14}, Lcom/regus/base/control/SpalashActivity;->access$200(Lcom/regus/base/control/SpalashActivity;ZLjava/lang/String;)V
    :try_end_164
    .catch Ljava/lang/Exception; {:try_start_155 .. :try_end_164} :catch_c1

    goto/16 :goto_ce

    .line 131
    .end local v4    # "e":Lorg/json/JSONException;
    .restart local v3    # "dataJsonObject":Lorg/json/JSONObject;
    .restart local v8    # "responseJson":Lorg/json/JSONObject;
    :cond_166
    :try_start_166
    iget-object v12, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    const/4 v13, 0x1

    iget-object v14, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    # getter for: Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;
    invoke-static {v14}, Lcom/regus/base/control/SpalashActivity;->access$000(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;

    move-result-object v14

    # invokes: Lcom/regus/base/control/SpalashActivity;->jump(ZLjava/lang/String;)V
    invoke-static {v12, v13, v14}, Lcom/regus/base/control/SpalashActivity;->access$200(Lcom/regus/base/control/SpalashActivity;ZLjava/lang/String;)V

    goto/16 :goto_ce

    .line 135
    .end local v3    # "dataJsonObject":Lorg/json/JSONObject;
    :cond_174
    iget-object v12, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    const/4 v13, 0x1

    iget-object v14, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    # getter for: Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;
    invoke-static {v14}, Lcom/regus/base/control/SpalashActivity;->access$000(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;

    move-result-object v14

    # invokes: Lcom/regus/base/control/SpalashActivity;->jump(ZLjava/lang/String;)V
    invoke-static {v12, v13, v14}, Lcom/regus/base/control/SpalashActivity;->access$200(Lcom/regus/base/control/SpalashActivity;ZLjava/lang/String;)V
    :try_end_180
    .catch Lorg/json/JSONException; {:try_start_166 .. :try_end_180} :catch_154
    .catch Ljava/lang/Exception; {:try_start_166 .. :try_end_180} :catch_c1

    goto/16 :goto_ce

    .line 144
    .end local v0    # "buffer":Ljava/lang/StringBuffer;
    .end local v1    # "bufferedReader":Ljava/io/BufferedReader;
    .end local v5    # "inputStream":Ljava/io/InputStream;
    .end local v6    # "jsonStr":Ljava/lang/String;
    .end local v7    # "line":Ljava/lang/String;
    .end local v8    # "responseJson":Lorg/json/JSONObject;
    :cond_182
    :try_start_182
    iget-object v12, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    const/4 v13, 0x1

    iget-object v14, p0, Lcom/regus/base/control/SpalashActivity$1;->this$0:Lcom/regus/base/control/SpalashActivity;

    # getter for: Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;
    invoke-static {v14}, Lcom/regus/base/control/SpalashActivity;->access$000(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;

    move-result-object v14

    # invokes: Lcom/regus/base/control/SpalashActivity;->jump(ZLjava/lang/String;)V
    invoke-static {v12, v13, v14}, Lcom/regus/base/control/SpalashActivity;->access$200(Lcom/regus/base/control/SpalashActivity;ZLjava/lang/String;)V
    :try_end_18e
    .catch Ljava/lang/Exception; {:try_start_182 .. :try_end_18e} :catch_c1

    goto/16 :goto_ce
.end method
