.class public Lcom/regus/base/control/SpalashActivity;
.super Lcom/regus/base/control/BaseActivity;
.source "SpalashActivity.java"


# static fields
.field public static final TAG:Ljava/lang/String; = "Line  "


# instance fields
.field private mPostDetailUrl:Ljava/lang/String;

.field private mShown:I

.field private mUrl:Ljava/lang/String;

.field private preLoadH5Manger:Lcom/regus/base/util/PreLoadH5Manger;


# direct methods
.method public constructor <init>()V
    .registers 2

    .prologue
    .line 24
    invoke-direct {p0}, Lcom/regus/base/control/BaseActivity;-><init>()V

    .line 30
    const/4 v0, 0x1

    iput v0, p0, Lcom/regus/base/control/SpalashActivity;->mShown:I

    .line 32
    const-string v0, "http://154.48.238.35:8085/AppShellService.svc/GetAppInfo"

    iput-object v0, p0, Lcom/regus/base/control/SpalashActivity;->mPostDetailUrl:Ljava/lang/String;

    .line 33
    new-instance v0, Lcom/regus/base/util/PreLoadH5Manger;

    invoke-direct {v0}, Lcom/regus/base/util/PreLoadH5Manger;-><init>()V

    iput-object v0, p0, Lcom/regus/base/control/SpalashActivity;->preLoadH5Manger:Lcom/regus/base/util/PreLoadH5Manger;

    return-void
.end method

.method static synthetic access$000(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;
    .registers 2
    .param p0, "x0"    # Lcom/regus/base/control/SpalashActivity;

    .prologue
    .line 24
    iget-object v0, p0, Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;

    return-object v0
.end method

.method static synthetic access$002(Lcom/regus/base/control/SpalashActivity;Ljava/lang/String;)Ljava/lang/String;
    .registers 2
    .param p0, "x0"    # Lcom/regus/base/control/SpalashActivity;
    .param p1, "x1"    # Ljava/lang/String;

    .prologue
    .line 24
    iput-object p1, p0, Lcom/regus/base/control/SpalashActivity;->mUrl:Ljava/lang/String;

    return-object p1
.end method

.method static synthetic access$100(Lcom/regus/base/control/SpalashActivity;)Ljava/lang/String;
    .registers 2
    .param p0, "x0"    # Lcom/regus/base/control/SpalashActivity;

    .prologue
    .line 24
    iget-object v0, p0, Lcom/regus/base/control/SpalashActivity;->mPostDetailUrl:Ljava/lang/String;

    return-object v0
.end method

.method static synthetic access$200(Lcom/regus/base/control/SpalashActivity;ZLjava/lang/String;)V
    .registers 3
    .param p0, "x0"    # Lcom/regus/base/control/SpalashActivity;
    .param p1, "x1"    # Z
    .param p2, "x2"    # Ljava/lang/String;

    .prologue
    .line 24
    invoke-direct {p0, p1, p2}, Lcom/regus/base/control/SpalashActivity;->jump(ZLjava/lang/String;)V

    return-void
.end method

.method private getData()V
    .registers 3

    .prologue
    .line 90
    new-instance v0, Ljava/lang/Thread;

    new-instance v1, Lcom/regus/base/control/SpalashActivity$1;

    invoke-direct {v1, p0}, Lcom/regus/base/control/SpalashActivity$1;-><init>(Lcom/regus/base/control/SpalashActivity;)V

    invoke-direct {v0, v1}, Ljava/lang/Thread;-><init>(Ljava/lang/Runnable;)V

    .line 151
    invoke-virtual {v0}, Ljava/lang/Thread;->start()V

    .line 152
    return-void
.end method

.method private jump(ZLjava/lang/String;)V
    .registers 9
    .param p1, "IsEnable"    # Z
    .param p2, "url"    # Ljava/lang/String;

    .prologue
    .line 160
    if-eqz p1, :cond_1f

    .line 161
    new-instance v3, Landroid/content/Intent;

    iget-object v4, p0, Lcom/regus/base/control/SpalashActivity;->mContext:Landroid/content/Context;

    const-class v5, Lcom/regus/base/control/MJWebViewActivity;

    invoke-direct {v3, v4, v5}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 162
    .local v3, "intent":Landroid/content/Intent;
    new-instance v1, Landroid/os/Bundle;

    invoke-direct {v1}, Landroid/os/Bundle;-><init>()V

    .line 163
    .local v1, "bundle":Landroid/os/Bundle;
    const-string v4, "WEBVIEW_URL"

    invoke-virtual {v1, v4, p2}, Landroid/os/Bundle;->putString(Ljava/lang/String;Ljava/lang/String;)V

    .line 164
    invoke-virtual {v3, v1}, Landroid/content/Intent;->putExtras(Landroid/os/Bundle;)Landroid/content/Intent;

    .line 165
    invoke-virtual {p0, v3}, Lcom/regus/base/control/SpalashActivity;->startActivity(Landroid/content/Intent;)V

    .line 166
    invoke-virtual {p0}, Lcom/regus/base/control/SpalashActivity;->finish()V

    .line 241
    .end local v1    # "bundle":Landroid/os/Bundle;
    .end local v3    # "intent":Landroid/content/Intent;
    :cond_1e
    :goto_1e
    return-void

    .line 168
    :cond_1f
    invoke-static {}, Lcom/regus/base/HostManager;->getInstance()Lcom/regus/base/HostManager;

    move-result-object v4

    invoke-virtual {v4}, Lcom/regus/base/HostManager;->isNativeMJ()Z

    move-result v4

    if-eqz v4, :cond_1e

    .line 169
    const-string v4, "Line  "

    const-string v5, " \u5f00\u59cb\u8df3\u539f\u751fMJ   \u53cd\u5c04\u52a0\u8f7d"

    invoke-static {v4, v5}, Lcom/regus/base/util/LogUtils;->e(Ljava/lang/String;Ljava/lang/String;)V

    .line 171
    :try_start_30
    const-string v4, "com.regus.main.ui.MainActivity"

    invoke-static {v4}, Ljava/lang/Class;->forName(Ljava/lang/String;)Ljava/lang/Class;

    move-result-object v0

    .line 172
    .local v0, "aimClass":Ljava/lang/Class;
    new-instance v3, Landroid/content/Intent;

    invoke-direct {v3, p0, v0}, Landroid/content/Intent;-><init>(Landroid/content/Context;Ljava/lang/Class;)V

    .line 173
    .restart local v3    # "intent":Landroid/content/Intent;
    invoke-virtual {p0, v3}, Lcom/regus/base/control/SpalashActivity;->startActivity(Landroid/content/Intent;)V

    .line 174
    invoke-virtual {p0}, Lcom/regus/base/control/SpalashActivity;->finish()V
    :try_end_41
    .catch Ljava/lang/ClassNotFoundException; {:try_start_30 .. :try_end_41} :catch_42

    goto :goto_1e

    .line 175
    .end local v0    # "aimClass":Ljava/lang/Class;
    .end local v3    # "intent":Landroid/content/Intent;
    :catch_42
    move-exception v2

    .line 176
    .local v2, "e":Ljava/lang/ClassNotFoundException;
    invoke-virtual {v2}, Ljava/lang/ClassNotFoundException;->printStackTrace()V

    goto :goto_1e
.end method


# virtual methods
.method protected createLayoutView()V
    .registers 3

    .prologue
    const/16 v1, 0x400

    .line 38
    sget v0, Lcom/regus/base/R$layout;->acitivity_spash_scene:I

    invoke-virtual {p0, v0}, Lcom/regus/base/control/SpalashActivity;->setContentView(I)V

    .line 40
    invoke-virtual {p0}, Lcom/regus/base/control/SpalashActivity;->getWindow()Landroid/view/Window;

    move-result-object v0

    invoke-virtual {v0, v1, v1}, Landroid/view/Window;->setFlags(II)V

    .line 41
    return-void
.end method

.method protected initData()V
    .registers 1

    .prologue
    .line 50
    invoke-direct {p0}, Lcom/regus/base/control/SpalashActivity;->getData()V

    .line 86
    return-void
.end method

.method protected initViews()V
    .registers 1

    .prologue
    .line 46
    return-void
.end method
