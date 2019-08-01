package ru.skillbranch.devintensive.ui.profile

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.TypedValue
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_profile.*
import ru.skillbranch.devintensive.R
import ru.skillbranch.devintensive.models.Profile
import ru.skillbranch.devintensive.utils.Utils
import ru.skillbranch.devintensive.viewmodels.ProfileViewModel
import kotlin.math.roundToInt

class ProfileActivity : AppCompatActivity() {

    companion object {
        const val IS_EDIT_MODE = "IS_EDIT_MODE"
    }

    private lateinit var viewModel: ProfileViewModel
    var isEditMode = false
    lateinit var viewFields: Map<String, TextView>

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initViews(savedInstanceState)
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean(IS_EDIT_MODE, isEditMode)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        viewModel.getProfileData().observe(this, Observer { updateUI(it) })
        viewModel.getTheme().observe(this, Observer { updateTheme(it) })
        viewModel.getRepositoryState().observe(this, Observer { upRepository(it) })
    }

    private fun upRepository(isError: Boolean) {
        wr_repository.isErrorEnabled = isError
        wr_repository.error = if (isError) "Невалидный адрес репозитория" else null
    }

    private fun updateTheme(mode: Int) {
        delegate.setLocalNightMode(mode)
    }

    private fun updateUI(profile: Profile) {
        profile.toMap().also {
            for ((k, v) in viewFields) {
                v.text = it[k].toString()
            }
            drawDefaultAvatar(it["initials"].toString())
        }
    }

    private fun initViews(savedInstanceState: Bundle?) {
        viewFields = mapOf(
                "nickName" to tv_nick_name,
                "rank" to tv_rank,
                "firstName" to et_first_name,
                "lastName" to et_last_name,
                "about" to et_about,
                "repository" to et_repository,
                "rating" to tv_rating,
                "respect" to tv_respect
        )

        isEditMode = savedInstanceState?.getBoolean(IS_EDIT_MODE, false) ?: false
        showCurrentMode(isEditMode)

        btn_edit.setOnClickListener {
            if (wr_repository.isErrorEnabled) {
                et_repository.text?.clear()
                viewModel.setRepositoryState(false)
            }
            if (isEditMode) saveProfileinfo()
            isEditMode = !isEditMode
            showCurrentMode(isEditMode)
        }
        btn_switch_theme.setOnClickListener {
            viewModel.switchTheme()
        }
        et_repository.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.setRepositoryState(!Utils.isRepositoryValid(s.toString()))
            }
        })
    }

    private fun showCurrentMode(isEdit: Boolean) {
        val info = viewFields.filter { setOf("firstName", "lastName", "about", "repository").contains(it.key) }
        for ((_, v) in info) {
            v as EditText
            v.isFocusable = isEdit
            v.isFocusableInTouchMode = isEdit
            v.isEnabled = isEdit
            v.background.alpha = if (isEdit) 255 else 0
        }

        ic_eye.visibility = if (isEdit) View.GONE else View.VISIBLE
        wr_about.isCounterEnabled = isEdit

        with(btn_edit) {
            val filter: ColorFilter? = if (isEdit) {
                PorterDuffColorFilter(
                        resources.getColor(R.color.color_accent, theme),
                        PorterDuff.Mode.SRC_IN
                )
            } else {
                null
            }
            val icon = if (isEdit) {
                resources.getDrawable(R.drawable.ic_save_black_24dp, theme)
            } else {
                resources.getDrawable(R.drawable.ic_edit_black_24dp, theme)
            }

            background.colorFilter = filter
            setImageDrawable(icon)
        }
    }

    private fun saveProfileinfo() {
        Profile(
                et_first_name.text.toString(),
                et_last_name.text.toString(),
                et_about.text.toString(),
                et_repository.text.toString()
        ).apply {
            viewModel.saveProfileData(this)
        }
    }

    private fun drawDefaultAvatar(initials: String, textSize: Float = 48f, color: Int = Color.WHITE) {
        val bitmap = textAsBitmap(initials, textSize, color)
        val drawable = BitmapDrawable(resources, bitmap)
        iv_avatar.setImageDrawable(drawable)
    }

    private fun textAsBitmap(text:String, textSize:Float, textColor:Int): Bitmap {
        val dp = resources.displayMetrics.density.roundToInt()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = textSize*dp
        paint.color = textColor
        paint.textAlign = Paint.Align.CENTER

        val image = Bitmap.createBitmap(112*dp, 112*dp, Bitmap.Config.ARGB_8888)

        image.eraseColor(getThemeAccentColor(this))
        val canvas = Canvas(image)
        canvas.drawText(text, 56f*dp, 56f*dp + paint.textSize/3, paint)
        return image
    }

    private fun getThemeAccentColor(context: Context): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, value, true)
        return value.data
    }
}
